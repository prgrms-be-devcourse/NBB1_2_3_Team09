package medinine.pill_buddy.domain.medicationApi.service

import medinine.pill_buddy.domain.medicationApi.dto.MedicationDTO
import medinine.pill_buddy.domain.medicationApi.dto.MedicationForm
import medinine.pill_buddy.domain.medicationApi.repository.MedicationApiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.net.URI

@SpringBootTest
@Transactional
internal class MedicationApiServiceTest{

    @Autowired
    lateinit var medicationApiService: MedicationApiService

    @Autowired
    lateinit var medicationRepository: MedicationApiRepository

    @MockBean
    lateinit var restTemplate: RestTemplate

    @BeforeEach
    fun setUp() {

        val medicationList =
            listOf(
                MedicationDTO(itemSeq = 19900505, itemName = "아스피린1", efcyQesitm = "확인용1"),
                MedicationDTO(itemSeq = 19900506, itemName = "아스피린2", efcyQesitm = "확인용2"),

            )

        medicationApiService.saveMedication(medicationList,"아스피린")

        val medications =
            listOf(

                MedicationDTO(itemSeq = 19900507, itemName = "아미노펜1", efcyQesitm = "확인용1"),
                MedicationDTO(itemSeq = 19900508, itemName = "아미노펜2", efcyQesitm = "확인용2"),
                MedicationDTO(itemSeq = 19900509, itemName = "아미노펜3", efcyQesitm = "확인용3")
            )

        medicationApiService.saveMedication(medications,"아미노펜")

        val jsonToString =
            "{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"pageNo\":1,\"totalCount\":1,\"numOfRows\":10,\"items\":[{\"entpName\":\"한국존슨앤드존슨판매(유)\",\"itemName\":\"어린이타이레놀산160밀리그램(아세트아미노펜)\",\"itemSeq\":\"202005623\",\"depositMethodQesitm\":\"실온에서 보관하십시오.\\n\\n어린이의 손이 닿지 않는 곳에 보관하십시오.\\n\",\"itemImage\":null,\"bizrno\":\"1068649891\"}]}}"

        val jsonToString2 =
            "{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"pageNo\":1,\"totalCount\":2,\"numOfRows\":10,\"items\":[{\"itemName\":\"아미노펜1\",\"itemSeq\":\"19900507\",\"efcyQesitm\":\"확인용변경1\"},{\"itemName\":\"아미노펜2\",\"itemSeq\":\"19900508\",\"efcyQesitm\":\"확인용변경2\"}]}}"

        val jsonToString3 =
            "{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"pageNo\":1,\"totalCount\":2,\"numOfRows\":10,\"items\":[{\"itemName\":\"아스피린1\",\"itemSeq\":\"19900505\",\"efcyQesitm\":\"확인용변경1\"},{\"itemName\":\"아스피린2\",\"itemSeq\":\"19900506\",\"efcyQesitm\":\"확인용변경2\"},{\"itemName\":\"아스피린3\",\"itemSeq\":\"19900509\",\"efcyQesitm\":\"확인용3\"}]}}"


        Mockito.`when`(
            restTemplate.getForObject(
                Mockito.argThat { uri: URI? -> uri.toString().contains("%ED%83%80%EC%9D%B4%EB%A0%88%EB%86%80") },
                Mockito.eq(
                    String::class.java
                )
            )
        ).thenReturn(jsonToString)

        Mockito.`when`(
            restTemplate.getForObject(
                Mockito.argThat { uri: URI? -> uri.toString().contains("%EC%95%84%EB%AF%B8%EB%85%B8%ED%8E%9C") },
                Mockito.eq(
                    String::class.java
                )
            )
        ).thenReturn(jsonToString2)

        Mockito.`when`(
            restTemplate.getForObject(
                Mockito.argThat { uri: URI? -> uri.toString().contains("%EC%95%84%EC%8A%A4%ED%94%BC%EB%A6%B0") },
                Mockito.eq(
                    String::class.java
                )
            )
        ).thenReturn(jsonToString3)
    }


    @Test
    @DisplayName("JSON을 파싱해 DB에 저장되고, 조회시 저장된 DTO가 반환돼야 한다.")
    fun testCreateDto_Success() {
        val medicationForm = MedicationForm("타이레놀")
        val dto = medicationApiService.createDto(medicationForm)
        assertThat(dto.size).isEqualTo(1)
        assertThat(dto[0].itemName).isEqualTo("어린이타이레놀산160밀리그램(아세트아미노펜)")
        medicationApiService.saveMedication(dto,"타이레놀")
        val findDto = medicationApiService.findPageByName("타이레놀", 0, 10)
        assertThat(findDto.content.size).isEqualTo(1)
        assertThat(findDto.content[0].itemName).isEqualTo("어린이타이레놀산160밀리그램(아세트아미노펜)")
        assertThat(findDto.totalPages).isEqualTo(1)
    }

    @Test
    @DisplayName("외부API의 데이터가 변경됐을 때, 데이터 동기화가 돼야한다.")
    fun testSynchronizedDB() {

        medicationApiService.synchronizeDB()

        assertThat(medicationRepository.findByItemSeq(19900505).efcyQesitm).isEqualTo("확인용변경1")
        assertThat(medicationRepository.findByItemSeq(19900505).modifiedAt).isNotNull()
        assertThat(medicationRepository.findByItemSeq(19900506).efcyQesitm).isEqualTo("확인용변경2")
        assertThat(medicationRepository.findByItemSeq(19900506).modifiedAt).isNotNull()
        assertThat(medicationRepository.findByItemSeq(19900507).efcyQesitm).isEqualTo("확인용변경1")
        assertThat(medicationRepository.findByItemSeq(19900508).efcyQesitm).isEqualTo("확인용변경2")
        assertThat(medicationRepository.findAllByItemName("아미노펜").size).isEqualTo(2)
        assertThat(medicationRepository.findAllByItemName("아스피린").size).isEqualTo(3)

    }


}