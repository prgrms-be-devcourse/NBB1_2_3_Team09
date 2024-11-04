package medinine.pill_buddy.domain.user.caregiver.repository

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.entity.CaretakerCaregiver
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerCaregiverRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.Test

@DataJpaTest
class CaregiverRepositoryTest @Autowired constructor(
    private val caretakerCaregiverRepository: CaretakerCaregiverRepository
) {

    private lateinit var caregiver: Caregiver
    private lateinit var caretaker: Caretaker

    @BeforeEach
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
    }

    @Test
    fun caretakerCaregiverRepositoryTest() {
        val caretakerCaregiver = CaretakerCaregiver(caregiver = caregiver, caretaker = caretaker)

        caretakerCaregiverRepository.save(caretakerCaregiver)
    }
}