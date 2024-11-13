package medinine.pill_buddy.domain.user.caregiver.controller

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.service.CaregiverService
import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.entity.MedicationType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class CaregiverControllerTest {

    @Mock
    private lateinit var caregiverService: CaregiverService

    @InjectMocks
    private lateinit var caregiverController: CaregiverController

    private lateinit var caregiver: Caregiver
    private lateinit var caretaker: Caretaker
    private lateinit var caretakerCaregiverDTO: CaretakerCaregiverDTO
    private lateinit var userMedicationDTO: UserMedicationDTO

    @BeforeEach
    fun setUp() {
        caregiver = Caregiver(
            id = 1L,
            username = "test-caregiver",
            loginId = "caregiver-login",
            password = "caregiver-password",
            email = "caregiver@example.com",
            phoneNumber = "010-1111-1111"
        )

        caretaker = Caretaker(
            id = 2L,
            username = "test-caretaker",
            loginId = "caretaker-login",
            password = "caretaker-password",
            email = "caretaker@example.com",
            phoneNumber = "010-2222-2222"
        )

        caretakerCaregiverDTO = CaretakerCaregiverDTO(
            id = 1L,
            caretaker = caretaker,
            caregiver = caregiver
        )

        userMedicationDTO = UserMedicationDTO(
            id = 1L,
            name = "Aspirin",
            dosage = 10,
            frequency = Frequency.TWICE_A_DAY,
            type = MedicationType.MEDICATION,
            stock = 10,
            expirationDate = LocalDateTime.now().plusYears(3),
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now().plusDays(5)
        )
    }

    @AfterEach
    fun tearDown() {
        reset(caregiverService)
    }

    @Test
    @DisplayName("Caretaker 복용약 조회")
    fun getCaretakerMedications() {
        `when`(caregiverService.getCaretakerMedications(caregiver.id!!, caretaker.id!!))
            .thenReturn(listOf(userMedicationDTO))

        val response: ResponseEntity<List<UserMedicationDTO>> = caregiverController.getCaretakerMedications(caregiver.id!!, caretaker.id!!)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(1, response.body?.size)
        assertEquals("Aspirin", response.body?.get(0)?.name)
        verify(caregiverService).getCaretakerMedications(caregiver.id!!, caretaker.id!!)
    }

    @Test
    @DisplayName("Caretaker 등록")
    fun addCaretaker() {
        `when`(caregiverService.register(caregiver.id!!, caretaker.id!!)).thenReturn(caretakerCaregiverDTO)

        val response: ResponseEntity<CaretakerCaregiverDTO> = caregiverController.addCaretaker(caregiver.id!!, caretaker.id!!)

        assertNotNull(response)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(caretakerCaregiverDTO, response.body)
        verify(caregiverService).register(caregiver.id!!, caretaker.id!!)
    }

    @Test
    @DisplayName("Caretaker 삭제")
    fun deleteCaretaker() {
        doNothing().`when`(caregiverService).remove(caregiver.id!!, caretaker.id!!)

        val response: ResponseEntity<Map<String, String>> = caregiverController.deleteCaretaker(caregiver.id!!, caretaker.id!!)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(mapOf("Process" to "Success"), response.body)
        verify(caregiverService).remove(caregiver.id!!, caretaker.id!!)
    }
}