package medinine.pill_buddy.domain.user.caregiver.service

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.entity.MedicationType
import medinine.pill_buddy.domain.userMedication.entity.UserMedication
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.test.Test

@SpringBootTest
class CaregiverServiceTest @Autowired constructor(
    private val caregiverService: CaregiverService,
    private val userMedicationRepository: UserMedicationRepository
) {

    private lateinit var caregiver: Caregiver
    private lateinit var caretaker: Caretaker

    @BeforeEach
    @Transactional
    fun setUp() {
        caregiver = Caregiver(
            username = "test-caregiver",
            loginId = "caregiver-login",
            password = "caregiver-password",
            email = "caregiver@example.com",
            phoneNumber = "010-1111-1111"
        )

        caretaker = Caretaker(
            username = "test-caretaker",
            loginId = "caretaker-login",
            password = "caretaker-password",
            email = "caretaker@example.com",
            phoneNumber = "010-2222-2222"
        )

        val medication = UserMedication(
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
        userMedicationRepository.save(medication)
    }

    @Test
    @Transactional
    fun getCaretakerMedicationsTests1() {   //정상적으로 불러지는지 확인
        val medications: List<UserMedicationDTO> = caregiverService.getCaretakerMedications(caregiver.id!!, caretaker.id!!)
        assertThat(medications[0].name).isEqualTo("Aspirin")
    }

    @Test
    @Transactional
    fun getCaretakerMedicationsTests2() {   //예외 - 등록되지 않은 사용자 검색했을 때
        val invalidCaretakerId = 99L
        assertThatThrownBy { caregiverService.getCaretakerMedications(caregiver.id!!, invalidCaretakerId) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage("사용자 정보가 일치하지 않습니다")
    }

    @Test
    @Transactional
    fun caregiverServiceTests1() {
        assertThatThrownBy { caregiverService.register(caregiver.id!!, caretaker.id!!) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage("이미 등록된 사용자 정보입니다")
    }

    @Test
    @Transactional
    fun caregiverServiceTests2() {
        val newCaregiverId = 2L
        val register: CaretakerCaregiverDTO = caregiverService.register(newCaregiverId, caretaker.id!!)

        assertThat(register.caregiver?.id).isEqualTo(newCaregiverId)
        assertThat(register.caretaker?.id).isEqualTo(caretaker.id)
    }

    @Test
    fun caregiverServiceTests3() {
        caregiverService.remove(caregiver.id!!, caretaker.id!!)
    }
}