package medinine.pill_buddy.domain.userMedication.controller

import jakarta.transaction.Transactional
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.entity.MedicationType
import medinine.pill_buddy.domain.userMedication.service.UserMedicationService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class UserMedicationControllerTest{

    @Autowired
    lateinit var userMedicationService: UserMedicationService

    @Test
    @Transactional
    fun createUserMedication(){
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
        val register = userMedicationService.register(caretakerId, userMedicationDTO)
        Assertions.assertThat(register.name).isEqualTo("감기약")
        Assertions.assertThat(register.description).isEqualTo("잘 듣는 감기약")
        Assertions.assertThat(register.dosage).isEqualTo(10)
        Assertions.assertThat(register.frequency).isEqualTo(Frequency.TWICE_A_DAY)
        Assertions.assertThat(register.type).isEqualTo(MedicationType.MEDICATION)
        Assertions.assertThat(register.stock).isEqualTo(5)
    }
}