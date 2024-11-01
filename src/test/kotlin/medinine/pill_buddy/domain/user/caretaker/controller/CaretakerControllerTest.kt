package medinine.pill_buddy.domain.user.caretaker.controller

import medinine.pill_buddy.domain.record.dto.RecordDTO
import medinine.pill_buddy.domain.record.entity.Taken
import medinine.pill_buddy.domain.userMedication.service.UserMedicationService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
class CaretakerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userMedicationService: UserMedicationService

    private lateinit var expectedRecords: List<RecordDTO>
    private val caretakerId = 1L
    private val date = LocalDate.of(2024, 9, 25)

    @BeforeEach
    fun setUp() {
        expectedRecords = listOf(
            RecordDTO(
                id = 1,
                date = LocalDateTime.of(2024, 9, 25, 9, 0),
                medicationName = "Aspirin",
                taken = Taken.TAKEN
            )
        )
        `when`(userMedicationService.getUserMedicationRecordsByDate(caretakerId, date.atStartOfDay())).thenReturn(
            expectedRecords
        )
    }

    @AfterEach
    fun tearDown() {
        reset(userMedicationService)
    }

    @Test
    @DisplayName("약 복용 여부 확인")
    fun getRecordsByDate() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/caretakers/$caretakerId/user-medications/records")
                .param("date", date.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(expectedRecords[0].id))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].date").value(expectedRecords[0].date.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].medicationName").value(expectedRecords[0].medicationName))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].taken").value(expectedRecords[0].taken.toString()))

        verify(userMedicationService).getUserMedicationRecordsByDate(caretakerId, date.atStartOfDay())
    }
}