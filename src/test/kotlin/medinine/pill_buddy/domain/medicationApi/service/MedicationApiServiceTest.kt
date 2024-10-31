package medinine.pill_buddy.domain.medicationApi.service

import medinine.pill_buddy.domain.medicationApi.dto.MedicationDTO
import medinine.pill_buddy.domain.medicationApi.dto.MedicationForm
import org.assertj.core.api.Assertions
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

    @MockBean
    lateinit var restTemplate: RestTemplate

    @BeforeEach
    fun setUp() {

        val medicationDTO1 = MedicationDTO(itemSeq = 19900505, itemName = "아스피린1")
        val medicationDTO2 = MedicationDTO(itemSeq = 19900506, itemName = "아스피린2")
        val medicationList = listOf(medicationDTO1,medicationDTO2)

        medicationApiService.saveMedication(medicationList)
        val jsonToString =
            "{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"pageNo\":1,\"totalCount\":1,\"numOfRows\":10,\"items\":[{\"entpName\":\"한국존슨앤드존슨판매(유)\",\"itemName\":\"어린이타이레놀산160밀리그램(아세트아미노펜)\",\"itemSeq\":\"202005623\",\"depositMethodQesitm\":\"실온에서 보관하십시오.\\n\\n어린이의 손이 닿지 않는 곳에 보관하십시오.\\n\",\"itemImage\":null,\"bizrno\":\"1068649891\"}]}}"
        Mockito.`when`(
            restTemplate.getForObject(
                Mockito.argThat { uri: URI -> uri.toString().contains("%ED%83%80%EC%9D%B4%EB%A0%88%EB%86%80") },
                Mockito.eq(
                    String::class.java
                )
            )
        ).thenReturn(jsonToString)
    }


    @Test
    @DisplayName("JSON을 파싱해 DB에 저장되고, 조회시 저장된 DTO가 반환돼야 한다.")
    fun testCreateDto_Success() {
        val medicationForm = MedicationForm("타이레놀")
        val dto = medicationApiService.createDto(medicationForm)
        Assertions.assertThat(dto.size).isEqualTo(1)
        Assertions.assertThat(dto[0].itemName).isEqualTo("어린이타이레놀산160밀리그램(아세트아미노펜)")
        medicationApiService.saveMedication(dto)
        val findDto = medicationApiService.findAllByName("타이레놀", 0, 10)
        Assertions.assertThat(findDto.content.size).isEqualTo(1)
        Assertions.assertThat(findDto.content[0].itemName).isEqualTo("어린이타이레놀산160밀리그램(아세트아미노펜)")
        Assertions.assertThat(findDto.totalPages).isEqualTo(1)
    }

}