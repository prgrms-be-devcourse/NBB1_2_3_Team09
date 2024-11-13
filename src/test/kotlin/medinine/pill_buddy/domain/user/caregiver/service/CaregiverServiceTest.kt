package medinine.pill_buddy.domain.user.caregiver.service

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.entity.CaretakerCaregiver
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerCaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CaregiverServiceTest {

    @Mock
    private lateinit var caretakerCaregiverRepository: CaretakerCaregiverRepository

    @Mock
    private lateinit var caregiverRepository: CaregiverRepository

    @Mock
    private lateinit var caretakerRepository: CaretakerRepository

    @Mock
    private lateinit var userMedicationRepository: UserMedicationRepository

    @InjectMocks
    private lateinit var caregiverService: CaregiverService

    private lateinit var caregiver: Caregiver
    private lateinit var caretaker: Caretaker
    private lateinit var caretakerCaregiver: CaretakerCaregiver

    @BeforeAll
    fun setUpMocks() {
        MockitoAnnotations.openMocks(this)
    }

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

        caretakerCaregiver = CaretakerCaregiver(
            id = 1L,
            caretaker = caretaker,
            caregiver = caregiver
        )
    }

    @AfterEach
    fun tearDown() {
        reset(caretakerCaregiverRepository, caregiverRepository, caretakerRepository, userMedicationRepository)
    }

    @Test
    @DisplayName("caretaker 등록")
    fun registerTest() {
        // given
        given(caretakerRepository.findById(caretaker.id!!)).willReturn(java.util.Optional.of(caretaker))
        given(caregiverRepository.findById(caregiver.id!!)).willReturn(java.util.Optional.of(caregiver))
        given(caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretaker.id!!, caregiver.id!!))
            .willReturn(null)
        given(caretakerCaregiverRepository.save(any(CaretakerCaregiver::class.java)))
            .willReturn(caretakerCaregiver)

        // when
        val result = caregiverService.register(caregiver.id!!, caretaker.id!!)

        // then
        assertNotNull(result)
        assertEquals(caretakerCaregiver.id, result.id)
        assertEquals(caretakerCaregiver.caretaker, result.caretaker)
        assertEquals(caretakerCaregiver.caregiver, result.caregiver)
    }

    @Test
    @DisplayName("이미 등록된 caretaker 등록")
    fun registerThrowExceptionTest() {
        // given
        given(caretakerRepository.findById(caretaker.id!!)).willReturn(java.util.Optional.of(caretaker))
        given(caregiverRepository.findById(caregiver.id!!)).willReturn(java.util.Optional.of(caregiver))
        given(caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretaker.id!!, caregiver.id!!))
            .willReturn(caretakerCaregiver)

        // when
        val exception = assertThrows<PillBuddyCustomException> {
            caregiverService.register(caregiver.id!!, caretaker.id!!)
        }

        // then
        assertEquals(ErrorCode.CARETAKER_ALREADY_REGISTERED, exception.errorCode)
    }

    @Test
    @DisplayName("caretaker 삭제")
    fun removeTest() {
        // given
        given(caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretaker.id!!, caregiver.id!!))
            .willReturn(caretakerCaregiver)

        // when
        caregiverService.remove(caregiver.id!!, caretaker.id!!)

        // then
        verify(caretakerCaregiverRepository).delete(caretakerCaregiver)
    }

    @Test
    @DisplayName("caretaker 삭제 - 예외")
    fun removeThrowExceptionTest() {
        // given
        given(caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretaker.id!!, caregiver.id!!))
            .willReturn(null)

        // when
        val exception = assertThrows<PillBuddyCustomException> {
            caregiverService.remove(caregiver.id!!, caretaker.id!!)
        }

        // then
        assertEquals(ErrorCode.CAREGIVER_CARETAKER_NOT_MATCHED, exception.errorCode)
    }
}