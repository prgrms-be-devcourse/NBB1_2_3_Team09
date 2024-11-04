package medinine.pill_buddy.domain.user.caretaker.controller

import medinine.pill_buddy.domain.record.dto.RecordDTO
import medinine.pill_buddy.domain.record.entity.Taken
import medinine.pill_buddy.domain.record.service.RecordService
import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.service.CaretakerService
import medinine.pill_buddy.domain.userMedication.service.UserMedicationService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class CaretakerControllerTest {

    @Mock
    private lateinit var caretakerService: CaretakerService

    @Mock
    private lateinit var userMedicationService: UserMedicationService

    @Mock
    private lateinit var recordService: RecordService

    @InjectMocks
    private lateinit var caretakerController: CaretakerController
    private lateinit var caretakerCaregiverDTO: CaretakerCaregiverDTO
    private lateinit var recordDTO: RecordDTO

    @BeforeEach
    fun setUp() {
        caretakerCaregiverDTO = CaretakerCaregiverDTO(
            id = 1L,
            caretaker = Caretaker(
                id = 1L,
                username = "test-caretaker",
                loginId = "caretaker-login",
                password = "caretaker-password",
                email = "caretaker@example.com",
                phoneNumber = "010-2222-2222"
            ),
            caregiver = Caregiver(
                id = 2L,
                username = "test-caregiver",
                loginId = "caregiver-login",
                password = "caregiver-password",
                email = "caregiver@example.com",
                phoneNumber = "010-1111-1111"
            )
        )

        recordDTO = RecordDTO(
            id = 1L,
            date = LocalDateTime.now(),
            medicationName = "Test Medication",
            taken = Taken.UNTAKEN
        )
    }


    @AfterEach
    fun tearDown() {
        reset(caretakerService, userMedicationService, recordService)
    }

    @Test
    @DisplayName("사용자의 보호자 등록")
    fun addCaregiver() {
        // given
        `when`(caretakerService.register(1L, 2L)).thenReturn(caretakerCaregiverDTO)

        // when
        val response = caretakerController.addCaregiver(1L, 2L)

        // then
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(caretakerCaregiverDTO, response.body)
        verify(caretakerService).register(1L, 2L)
    }

    @Test
    @DisplayName("사용자의 보호자 삭제")
    fun deleteCaregiver() {
        // given
        doNothing().`when`(caretakerService).remove(1L, 2L)

        // when
        val response = caretakerController.deleteCaregiver(1L, 2L)

        // then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(mapOf("Process" to "Success"), response.body)
        verify(caretakerService).remove(1L, 2L)
    }

    @Test
    @DisplayName("날짜를 통해 복용 여부 확인")
    fun getRecordsByDate() {
        // given
        val date = LocalDate.now()
        val records = listOf(recordDTO)
        `when`(userMedicationService.getUserMedicationRecordsByDate(1L, date.atStartOfDay())).thenReturn(records)

        // when
        val response = caretakerController.getRecordsByDate(1L, date)

        // then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(records, response.body)
        verify(userMedicationService).getUserMedicationRecordsByDate(1L, date.atStartOfDay())
    }

    @Test
    @DisplayName("약 정보 기록 생성")
    fun addRecord() {
        // given
        `when`(recordService.registerRecord(3L)).thenReturn(recordDTO)

        // when
        val response = caretakerController.addRecord(3L)

        // then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(recordDTO, response.body)
        verify(recordService).registerRecord(3L)
    }

    @Test
    @DisplayName("약 정보 기록 수정")
    fun updateRecord() {
        // given
        `when`(recordService.modifyTaken(3L, 1L)).thenReturn(recordDTO)

        // when
        val response = caretakerController.updateRecord(3L, 1L)

        // then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(recordDTO, response.body)
        verify(recordService).modifyTaken(3L, 1L)
    }
}