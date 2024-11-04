package medinine.pill_buddy.domain.user.caregiver.controller

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.entity.MedicationType
import medinine.pill_buddy.domain.userMedication.entity.UserMedication
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class CaregiverControllerTest {

    @Autowired
    private lateinit var caregiverController: CaregiverController

    @Autowired
    private lateinit var caregiverRepository: CaregiverRepository

    @Autowired
    private lateinit var caretakerRepository: CaretakerRepository

    @Autowired
    private lateinit var userMedicationRepository: UserMedicationRepository

    private lateinit var caretaker1: Caretaker
    private lateinit var caregiver1: Caregiver
    private lateinit var caretaker2: Caretaker
    private lateinit var caregiver2: Caregiver
    private lateinit var userMedication: UserMedication

    @BeforeEach
    fun setup() {
        caretaker1 = caretakerRepository.save(createCaretaker("test-caretaker1", "caretaker1-loginId", "caretaker1@example.com"))
        caregiver1 = caregiverRepository.save(createCaregiver("test-caregiver1", "caregiver1-loginId", "caregiver1@example.com"))
        caretaker2 = caretakerRepository.save(createCaretaker("test-caretaker2", "caretaker2-loginId", "caretaker2@example.com"))
        caregiver2 = caregiverRepository.save(createCaregiver("test-caregiver2", "caregiver2-loginId", "caregiver2@example.com"))

        userMedication = userMedicationRepository.save(createUserMedication(caretaker1))
    }

    private fun createCaretaker(username: String, loginId: String, email: String): Caretaker {
        return Caretaker(
            username = username,
            loginId = loginId,
            password = "password",
            email = email,
            phoneNumber = "010-1111-1111"
        )
    }

    private fun createCaregiver(username: String, loginId: String, email: String): Caregiver {
        return Caregiver(
            username = username,
            loginId = loginId,
            password = "password",
            email = email,
            phoneNumber = "010-2222-2222"
        )
    }

    private fun createUserMedication(caretaker: Caretaker): UserMedication {
        return UserMedication(
            name = "Aspirin",
            dosage = 10,
            frequency = Frequency.TWICE_A_DAY,
            type = MedicationType.MEDICATION,
            stock = 10,
            expirationDate = LocalDateTime.now().plusYears(3),
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now().plusDays(5),
            caretaker = caretaker
        )
    }

    @Test
    fun getCaretakerMedications() {
        val caregiverId = caregiver1.id!!
        val caretakerId = caretaker1.id!!

        val userMedication: ResponseEntity<List<UserMedicationDTO>> = caregiverController.getCaretakerMedications(caregiverId, caretakerId)

        assertThat(userMedication).isNotNull
        assertThat(userMedication.body?.get(0)?.name).isEqualTo("Aspirin")
    }

    @Test
    fun addCaretaker() {
        val caregiverId = caregiver2.id!!
        val caretakerId = caretaker1.id!!

        val caretakerCaregiver: ResponseEntity<CaretakerCaregiverDTO> = caregiverController.addCaretaker(caregiverId, caretakerId)

        assertThat(caretakerCaregiver).isNotNull
        assertThat(caretakerCaregiver.body?.caretaker?.username).isEqualTo(caretaker1.username)
    }

    @Test
    fun deleteCaretaker() {
        val caregiverId = caregiver2.id!!
        val caretakerId = caretaker1.id!!

        caregiverController.addCaretaker(caregiverId, caretakerId)

        val response: ResponseEntity<Map<String, String>> = caregiverController.deleteCaretaker(caregiverId, caretakerId)

        // Then
        assertThat(response).isNotNull
        assertThat(response.body).containsEntry("Process", "Success")
    }
}