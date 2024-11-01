package medinine.pill_buddy.domain.user.caretaker.controller

import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import org.junit.jupiter.api.DisplayName
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class CaretakerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var caretakerController: CaretakerController

    @Test
    @DisplayName("사용자 기능 중 보호자 추가 기능 테스트")
    fun addCaregiver() {
        val caretakerId = 2L
        val caregiverId = 1L
        val response = ResponseEntity(CaretakerCaregiverDTO(), HttpStatus.OK)

        `when`(caretakerController.addCaregiver(caretakerId, caregiverId)).thenReturn(response)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/caretakers/$caretakerId/caregivers/$caregiverId"))
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("사용자 기능 중 보호자 삭제 기능 테스트")
    fun deleteCaregiver() {
        val caretakerId = 1L
        val caregiverId = 1L
        val response = ResponseEntity(mapOf("Process" to "Success"), HttpStatus.OK)

        `when`(caretakerController.deleteCaregiver(caretakerId, caregiverId)).thenReturn(response)

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/caretakers/$caretakerId/caregivers/$caregiverId"))
            .andExpect(status().isOk)
    }
}