package medinine.pill_buddy.domain.user.caretaker.service

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.entity.CaretakerCaregiver
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerCaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
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
class CaretakerServiceImplTest {

    @Mock
    private lateinit var caretakerCaregiverRepository: CaretakerCaregiverRepository

    @Mock
    private lateinit var caretakerRepository: CaretakerRepository

    @Mock
    private lateinit var caregiverRepository: CaregiverRepository

    @InjectMocks
    private lateinit var caretakerService: CaretakerServiceImpl
    private lateinit var caretaker: Caretaker
    private lateinit var caregiver: Caregiver
    private lateinit var caretakerCaregiver: CaretakerCaregiver

    @BeforeAll
    fun setUpMocks() {
        MockitoAnnotations.openMocks(this)
    }

    @BeforeEach
    fun setUp() {
        caretaker = Caretaker(
            id = 1L,
            username = "test-caretaker",
            loginId = "caretaker-login",
            password = "caretaker-password",
            email = "caretaker@example.com",
            phoneNumber = "010-2222-2222"
        )

        caregiver = Caregiver(
            id = 2L,
            username = "test-caregiver",
            loginId = "caregiver-login",
            password = "caregiver-password",
            email = "caregiver@example.com",
            phoneNumber = "010-1111-1111"
        )

        caretakerCaregiver = CaretakerCaregiver(
            id = 1L,
            caretaker = caretaker,
            caregiver = caregiver
        )
    }

    @AfterEach
    fun tearDown() {
        reset(caretakerCaregiverRepository, caretakerRepository, caregiverRepository)
    }

    @Test
    @DisplayName("보호자 등록 테스트")
    fun registerTest() {
        // given
        given(caretakerRepository.findById(caretaker.id!!)).willReturn(java.util.Optional.of(caretaker))
        given(caregiverRepository.findById(caregiver.id!!)).willReturn(java.util.Optional.of(caregiver))
        given(caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretaker.id!!, caregiver.id!!))
            .willReturn(null)
        given(caretakerCaregiverRepository.save(any(CaretakerCaregiver::class.java)))
            .willReturn(caretakerCaregiver)

        // when
        val result = caretakerService.register(caretaker.id!!, caregiver.id!!)

        // then
        assertNotNull(result)
        assertEquals(caretakerCaregiver.id, result.id)
        assertEquals(caretakerCaregiver.caretaker, result.caretaker)
        assertEquals(caretakerCaregiver.caregiver, result.caregiver)
    }

    @Test
    @DisplayName("보호자 등록 예외 테스트")
    fun registerThrowExceptionTest() {
        // given
        given(caretakerRepository.findById(caretaker.id!!)).willReturn(java.util.Optional.of(caretaker))
        given(caregiverRepository.findById(caregiver.id!!)).willReturn(java.util.Optional.of(caregiver))
        given(caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretaker.id!!, caregiver.id!!))
            .willReturn(caretakerCaregiver)

        // when
        val exception = assertThrows<PillBuddyCustomException> {
            caretakerService.register(caretaker.id!!, caregiver.id!!)
        }

        // then
        assertEquals(ErrorCode.CARETAKER_CAREGIVER_NOT_REGISTERED, exception.errorCode)
    }

    @Test
    @DisplayName("보호자 삭제 테스트")
    fun removeTest() {
        // given
        given(caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretaker.id!!, caregiver.id!!))
            .willReturn(caretakerCaregiver)

        // when
        caretakerService.remove(caretaker.id!!, caregiver.id!!)

        // then
        verify(caretakerCaregiverRepository).delete(caretakerCaregiver)
    }

    @Test
    @DisplayName("보호자 삭제 예외 테스트")
    fun removeThrowException() {
        // given
        given(caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretaker.id!!, caregiver.id!!))
            .willReturn(null)

        // when
        val exception = assertThrows<PillBuddyCustomException> {
            caretakerService.remove(caretaker.id!!, caregiver.id!!)
        }

        // then
        assertEquals(ErrorCode.CARETAKER_CAREGIVER_NOT_MATCHED, exception.errorCode)
    }
}
