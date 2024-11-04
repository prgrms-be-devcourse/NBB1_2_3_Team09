package medinine.pill_buddy.domain.userMedication.service

import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.entity.UserMedication
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
class UserMedicationServiceImplTest {

    @Mock
    private lateinit var userMedicationRepository: UserMedicationRepository

    @Mock
    private lateinit var caretakerRepository: CaretakerRepository

    @InjectMocks
    private lateinit var userMedicationService: UserMedicationServiceImpl
    private lateinit var caretaker: Caretaker
    private lateinit var userMedicationDTO: UserMedicationDTO
    private lateinit var userMedication: UserMedication

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

        userMedicationDTO = UserMedicationDTO(
            id = 1L,
            name = "Test Medication",
            description = "Test Description",
            dosage = 50
        )

        userMedication = userMedicationDTO.toEntity().apply {
            caretaker?.let { updateCaretaker(it) }
        }
    }

    @AfterEach
    fun tearDown() {
        reset(userMedicationRepository, caretakerRepository)
    }

    @Test
    @DisplayName("등록 테스트")
    fun registerTest() {
        // given
        given(caretakerRepository.findById(caretaker.id!!)).willReturn(java.util.Optional.of(caretaker))
        given(userMedicationRepository.save(any(UserMedication::class.java))).willReturn(userMedication)

        // when
        val result = userMedicationService.register(caretaker.id!!, userMedicationDTO)

        // then
        assertNotNull(result)
        assertEquals(userMedicationDTO.name, result.name)
        assertEquals(userMedicationDTO.description, result.description)
    }

    @Test
    @DisplayName("등록 예외 테스트")
    fun registerException() {
        // given
        given(caretakerRepository.findById(caretaker.id!!)).willReturn(java.util.Optional.empty())

        // when & then
        val exception = assertThrows<PillBuddyCustomException> {
            userMedicationService.register(caretaker.id!!, userMedicationDTO)
        }
        assertEquals(ErrorCode.CARETAKER_NOT_FOUND, exception.errorCode)
    }

    @Test
    @DisplayName("약 정보 기록 반환")
    fun retrieveUserMedication() {
        // given
        given(userMedicationRepository.findByCaretakerId(caretaker.id!!)).willReturn(listOf(userMedication))

        // when
        val result = userMedicationService.retrieve(caretaker.id!!)

        // then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(userMedicationDTO.name, result[0].name)
    }
}