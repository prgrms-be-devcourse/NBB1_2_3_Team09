package medinine.pill_buddy.domain.userMedication.controller

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.entity.MedicationType
import medinine.pill_buddy.domain.userMedication.entity.UserMedication
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "mock-user", roles = ["USER"])
class UserMedicationControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val userMedicationRepository: UserMedicationRepository,
    private val caretakerRepository: CaretakerRepository
) {
    private lateinit var caretaker: Caretaker
    private lateinit var userMedication: UserMedication

    @BeforeEach
    fun setup() {
        createCaretaker()
        caretakerRepository.save(caretaker)
        createUserMedications()
        userMedicationRepository.save(userMedication)
    }

    @Test
    @DisplayName("약 정보 등록 API 테스트")
    fun createUserMedication() {
        // given
        val caretakerId = caretaker.id!!
        val request = UserMedicationDTO(
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

        // when
        val result = mockMvc.perform(
            post("/api/caretakers/{caretakerId}/user-medications", caretakerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andReturn().response
        val jsonResponse = result.contentAsString
        val response = objectMapper.readValue(jsonResponse, UserMedicationDTO::class.java)

        // then
        assertThat(result.status).isEqualTo(201)
        assertThat(request.dosage).isEqualTo(response.dosage)
        assertThat(request.type).isEqualTo(response.type)
        assertThat(request.stock).isEqualTo(response.stock)
        assertThat(request.startDate).isEqualTo(response.startDate)
        assertThat(request.endDate).isEqualTo(response.endDate)
    }

    @Test
    @Transactional
    @DisplayName("약 정보 조회 API 테스트")
    fun getUserMedication() {
        // given
        val caretakerId = caretaker.id!!

        // when
        val result = mockMvc.perform(get("/api/caretakers/{caretakerId}/user-medications", caretakerId)
                .accept(MediaType.APPLICATION_JSON)).andReturn().response

        val jsonResponse = result.contentAsString
        val responseList = objectMapper.readValue(jsonResponse, Array<UserMedicationDTO>::class.java).toList()

        // then
        assertThat(result.status).isEqualTo(200)
        assertThat(responseList).hasSize(1)

        val response = responseList[0]
        assertThat(response.id).isEqualTo(userMedication.id)
        assertThat(response.name).isEqualTo(userMedication.name)
        assertThat(response.description).isEqualTo(userMedication.description)
        assertThat(response.dosage).isEqualTo(userMedication.dosage)
        assertThat(response.frequency).isEqualTo(userMedication.frequency)
        assertThat(response.type).isEqualTo(userMedication.type)
        assertThat(response.stock).isEqualTo(userMedication.stock)
    }

    @Test
    @DisplayName("약 정보 수정 API 테스트")
    fun updateUserMedication() {
        // given
        val caretakerId = caretaker.id!!
        val userMedicationId = userMedication.id!!

        val request = UserMedicationDTO(
            name = "vitamin",
            description = "vitaminA",
            dosage = 5
        )

        // when
        val result = mockMvc.perform(
            put("/api/caretakers/{caretakerId}/user-medications/{userMedicationId}", caretakerId, userMedicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andReturn().response
        val jsonResponse = result.contentAsString
        val response = objectMapper.readValue(jsonResponse, UserMedicationDTO::class.java)

        // then
        assertThat(result.status).isEqualTo(200)
        assertThat(response.name).isEqualTo(request.name)
        assertThat(response.description).isEqualTo(request.description)
        assertThat(response.dosage).isEqualTo(request.dosage)
    }

    private fun createCaretaker() {
        caretaker = Caretaker(
            username = "test-caretaker",
            loginId = "test-loginId",
            password = "test-password",
            email = "test-email",
            phoneNumber = "test-phoneNumber"
        )
    }

    private fun createUserMedications() {
        userMedication = UserMedication(
            name = "test-medication-name",
            description = "test-medication-description",
            dosage = 10,
            frequency = Frequency.TWICE_A_DAY,
            type = MedicationType.MEDICATION,
            stock = 5,
            expirationDate = LocalDateTime.now(),
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now(),
            caretaker = caretaker
        )
    }
}
