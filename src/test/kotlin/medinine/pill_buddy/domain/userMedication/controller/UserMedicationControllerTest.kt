package medinine.pill_buddy.domain.userMedication.controller

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.entity.MedicationType
import medinine.pill_buddy.domain.userMedication.service.UserMedicationService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
class UserMedicationControllerTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userMedicationService: UserMedicationService

    @Test
    @Transactional
    @DisplayName("약 정보 등록 컨트롤러")
    fun createUserMedication() {
        val caretakerId = 2L
        val userMedicationDTO = UserMedicationDTO(
            name = "감기약",
            description = "잘 듣는 감기약",
            dosage = 10,
            frequency = Frequency.TWICE_A_DAY,
            type = MedicationType.MEDICATION,
            stock = 5,
            expirationDate = LocalDateTime.now(),
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        )

        whenever(userMedicationService.retrieve(caretakerId)).thenReturn(listOf(userMedicationDTO))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/caretakers/$caretakerId/user-medications")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("감기약"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("잘 듣는 감기약"))
    }
    @Test
    @DisplayName("약 정보 조회 컨트롤러")
    fun getUserMedication() {
        val caretakerId = 2L
        val userMedicationDTO = UserMedicationDTO(
            id = 2L,
            name = "감기약",
            description = "잘 듣는 감기약",
            dosage = 10,
            frequency = Frequency.TWICE_A_DAY,
            type = MedicationType.MEDICATION,
            stock = 5,
            expirationDate = LocalDateTime.now(),
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        )

        whenever(userMedicationService.retrieve(caretakerId)).thenReturn(listOf(userMedicationDTO))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/caretakers/$caretakerId/user-medications")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2L))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("감기약"))
    }

    @Test
    @Transactional
    fun updateUserMedication() {
        val caretakerId = 1L
        val medicationId = 1L
        val userMedicationDTO = UserMedicationDTO(
            name = "vitamin",
            description = "vitaminA",
            dosage = 5,
            frequency = Frequency.TWICE_A_DAY,
            type = MedicationType.SUPPLEMENT,
            stock = 5,
            expirationDate = LocalDateTime.now(),
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        )

        whenever(userMedicationService.modify(caretakerId, medicationId, userMedicationDTO)).thenReturn(userMedicationDTO)

        val modify = userMedicationService.modify(caretakerId, medicationId, userMedicationDTO)
        Assertions.assertThat(modify.name).isEqualTo("vitamin")
        Assertions.assertThat(modify.description).isEqualTo("vitaminA")
        Assertions.assertThat(modify.dosage).isEqualTo(5)
    }
}
