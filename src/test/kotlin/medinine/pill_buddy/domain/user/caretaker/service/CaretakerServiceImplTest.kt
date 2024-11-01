package medinine.pill_buddy.domain.user.caretaker.service

import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class CaretakerServiceImplTest {

    @Autowired
    private lateinit var caretakerService: CaretakerService

    @Test
    @DisplayName("보호자 등록 테스트")
    fun addCaregiverTest() {
        val caretakerId = 1L
        val caregiverId = 3L

        val register: CaretakerCaregiverDTO = caretakerService.register(caretakerId, caregiverId)

        assertThat(register.caretaker?.id).isEqualTo(1L)
        assertThat(register.caregiver?.id).isEqualTo(3L)
    }
}