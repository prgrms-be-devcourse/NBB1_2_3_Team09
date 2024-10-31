package medinine.pill_buddy.domain.userMedication.service

import jakarta.transaction.Transactional
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.entity.MedicationType
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import kotlin.test.Test

@SpringBootTest
class UserMedicationServiceImplTest {
    @Autowired
    private lateinit var userMedicationService: UserMedicationService

    @Autowired
    private lateinit var userMedicationRepository: UserMedicationRepository

    @Test
    @Transactional
    @DisplayName("약 정보 등록 테스트")
    fun userMedicationRegister() {
        val caretakerId = 2L
        val userMedicationDTO = UserMedicationDTO(
            name = "텐텐",
            description = "맛있는 영양제",
            dosage = 3,
            frequency = Frequency.TWICE_A_DAY,
            type = MedicationType.SUPPLEMENT,
            stock = 10,
            expirationDate = LocalDateTime.now(),
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        )

        val register = userMedicationService.register(caretakerId, userMedicationDTO)
        assertThat(register.name).isEqualTo("텐텐")
        assertThat(register.description).isEqualTo("맛있는 영양제")
        assertThat(register.dosage).isEqualTo(3)
        assertThat(register.frequency).isEqualTo(Frequency.TWICE_A_DAY)
        assertThat(register.type).isEqualTo(MedicationType.SUPPLEMENT)
        assertThat(register.stock).isEqualTo(10)
    }

    @Test
    @DisplayName("약 정보 조회")
    fun userMedicationRetrieve() {
        val caretakerId = 2L

        val byCaretakerId = userMedicationRepository.findByCaretakerId(caretakerId)
        if (byCaretakerId.isEmpty()) {
            return
        }

        assertThat(byCaretakerId[0].name).isEqualTo("Vitamin D")
        assertThat(byCaretakerId[0].dosage).isEqualTo(1)
        assertThat(byCaretakerId[0].description).isEqualTo("Bone health supplement")
        assertThat(byCaretakerId[0].caretaker?.id).isEqualTo(caretakerId)
    }

    @Test
    @Transactional
    fun userMedicationUpdate() {
        val caretakerId = 1L
        val userMedicationId = 1L
        val byCaretakerId = userMedicationRepository.findByCaretakerId(caretakerId)

        if (byCaretakerId[0].id == userMedicationId) {
            val userMedication = byCaretakerId[0]
            userMedication.updateName("aspirin")
            userMedication.updateDosage(100)
            userMedication.updateDescription("loose pain")
        }

        val updatedMedication = userMedicationRepository.findById(userMedicationId).get()
        assertThat(updatedMedication.name).isEqualTo("aspirin")
        assertThat(updatedMedication.dosage).isEqualTo(100)
        assertThat(updatedMedication.description).isEqualTo("loose pain")
    }
}