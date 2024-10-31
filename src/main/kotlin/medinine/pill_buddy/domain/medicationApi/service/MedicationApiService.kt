package medinine.pill_buddy.domain.medicationApi.service


import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import medinine.pill_buddy.domain.medicationApi.dto.MedicationDTO
import medinine.pill_buddy.domain.medicationApi.dto.MedicationForm
import medinine.pill_buddy.domain.medicationApi.entity.Medication
import medinine.pill_buddy.domain.medicationApi.repository.MedicationApiRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.net.SocketTimeoutException
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.ceil

@Service
class MedicationApiService(
    private val medicationApiRepository: MedicationApiRepository,
    private val objectMapper: ObjectMapper,
    private val restTemplate: RestTemplate,
    private val modelMapper: ModelMapper

) {
    private val numOfRows = 100

    @Value("\${openApi.dataType}")
    private var dataType: String? = null

    @Value("\${openApi.serviceKey}")
    private var serviceKey: String? = null

    @Value("\${openApi.callbackUrl}")
    private var callbackUrl: String? = null

    fun createDto(medicationForm: MedicationForm): List<MedicationDTO> {
        try {
            var jsonToString = restTemplate.getForObject(createUrl(medicationForm, 1), String::class.java)
            val totalCount = getTotalCount(jsonToString?:throw PillBuddyCustomException(ErrorCode.ERROR_CONNECTION))
            val medicationDTOList = getDtoFromApi(jsonToString)
            if (totalCount > numOfRows) {
                var i = 2
                while (i <= ceil(totalCount / numOfRows)) {
                    jsonToString = restTemplate.getForObject(createUrl(medicationForm, i), String::class.java)
                    medicationDTOList.addAll(getDtoFromApi(jsonToString?:throw PillBuddyCustomException(ErrorCode.ERROR_CONNECTION)))
                    i++
                }
            }
            return medicationDTOList
        } catch (e: JsonProcessingException) {
            throw PillBuddyCustomException(ErrorCode.MEDICATION_NOT_FOUND)
        } catch (e: SocketTimeoutException) {
            throw PillBuddyCustomException(ErrorCode.NETWORK_ERROR)
        } catch (e: ResourceAccessException) {
            throw PillBuddyCustomException(ErrorCode.ERROR_CONNECTION)
        }
    }

    @Transactional(readOnly = true)
    fun findAllByName(itemName: String, pageNo: Int, numOfRows: Int): Page<MedicationDTO> {
        val pageRequest = PageRequest.of(pageNo, numOfRows, Sort.by(Sort.Direction.ASC, "itemSeq"))
        val allByItemNameLike = medicationApiRepository.findAllByItemNameLike(itemName, pageRequest)
        if(pageNo> allByItemNameLike.totalPages) throw PillBuddyCustomException(ErrorCode.OUT_OF_PAGE)
        return allByItemNameLike.map { modelMapper.map(it,MedicationDTO::class.java) }
    }

    @Transactional
    fun saveMedication(medicationDTOList: List<MedicationDTO>) {
        val medicationList = medicationDTOList.map { modelMapper.map(it,Medication::class.java) }
        medicationApiRepository.saveAll(medicationList)
    }

    @Throws(JsonProcessingException::class)
    private fun getTotalCount(jsonToString: String): Double {
        return objectMapper.readValue(
            objectMapper.readTree(jsonToString).path("body").path("totalCount").toString(),
            Double::class.java
        )
    }

    @Throws(JsonProcessingException::class)
    private fun getDtoFromApi(jsonToString: String): MutableList<MedicationDTO> {
        return objectMapper.readValue(
            objectMapper.readTree(jsonToString).path("body").path("items").toString(),
            object : TypeReference<MutableList<MedicationDTO>>() {})
    }

    private fun createUrl(medicationForm: MedicationForm, pageNo: Int): URI {
        val encodedItemName = stringEncoding(medicationForm.itemName)
        var url =
            "$callbackUrl?serviceKey=$serviceKey&type=$dataType&itemName=$encodedItemName&pageNo=$pageNo&numOfRows=$numOfRows"
        if (medicationForm.entpName != null) {
            val encodedEntpName = stringEncoding(medicationForm.entpName)
            url += "&entpName=$encodedEntpName"
        }
        if (medicationForm.itemSeq != null) {
            val encodedItemSeq = stringEncoding(medicationForm.itemSeq)
            url += "&itemSeq=$encodedItemSeq"
        }
        return URI.create(url)
    }

    private fun stringEncoding(parameter: String): String {
        return URLEncoder.encode(parameter, StandardCharsets.UTF_8)
    }
}

