package medinine.pill_buddy.domain.user.service

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.dto.UserUpdateDto
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UserServiceTest(
    @Autowired
    private val userService: UserService,

    @Autowired
    private val caretakerRepository: CaretakerRepository,

    @Autowired
    private val caregiverRepository: CaregiverRepository,

    @Autowired
    private val passwordEncoder: PasswordEncoder,
) {
    private lateinit var caretaker: Caretaker
    private lateinit var caregiver: Caregiver

    @BeforeEach
    fun setup() {
        createCaretaker()
        createCaregiver()
        caretakerRepository.save(caretaker)
        caregiverRepository.save(caregiver)
    }

    @Test
    @DisplayName("사용자를 조회할 수 있다.")
    fun findCaretaker() {
        // given
        val userId = caretaker.id
        val userType = UserType.valueOf(caretaker.javaClass.simpleName.uppercase().uppercase())

        // when
        val userDto = userService.findUser(userId!!, userType)

        // then
        assertThat(userDto.username).isEqualTo(caretaker.username)
        assertThat(userDto.phoneNumber).isEqualTo(caretaker.phoneNumber)
        assertThat(userDto.email).isEqualTo(caretaker.email)
        assertThat(userDto.userType).isEqualTo(userType.toString().lowercase())
    }

    @Test
    @DisplayName("보호자를 조회할 수 있다.")
    fun findCaregiver() {
        // given
        val userId = caregiver.id
        val userType = UserType.valueOf(caregiver.javaClass.simpleName.uppercase())

        // when
        val userDto = userService.findUser(userId!!, userType)

        // then
        assertThat(userDto.username).isEqualTo(caregiver.username)
        assertThat(userDto.phoneNumber).isEqualTo(caregiver.phoneNumber)
        assertThat(userDto.email).isEqualTo(caregiver.email)
        assertThat(userDto.userType).isEqualTo(userType.toString().lowercase())
    }

    @Test
    @DisplayName("등록되지 않은 사용자를 조회하면 PillBuddyCustomException이 발생한다.")
    fun findCaretaker_with_invalid_caretaker() {
        val userId = 99999L
        val userType = UserType.CARETAKER

        assertThatThrownBy { userService.findUser(userId, userType) }
            .isExactlyInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage(ErrorCode.USER_NOT_FOUND.message)
    }

    @Test
    @DisplayName("등록되지 않은 보호자를 조회하면 PillBuddyCustomException이 발생한다.")
    fun findCaregiver_with_invalid_caregiver() {
        val userId = 99999L
        val userType = UserType.CAREGIVER

        assertThatThrownBy { userService.findUser(userId, userType) }
            .isExactlyInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage(ErrorCode.USER_NOT_FOUND.message)
    }

    @Test
    @DisplayName("회원 정보를 수정할 수 있다.")
    fun updateUserInfo() {
        // given
        val userId = caretaker.id
        val userType = UserType.valueOf(caretaker.javaClass.simpleName.uppercase())
        val updateUsername = "new-username"
        val updateLoginId = "new-loginId"
        val updateEmail = "new-email"
        val updatePhoneNumber = "new-phoneNumber"

        val userUpdateDto = UserUpdateDto(
            username = updateUsername,
            loginId = updateLoginId,
            email = updateEmail,
            phoneNumber = updatePhoneNumber,
            userType = userType
        )

        // when
        val userDto = userService.updateUserInfo(userId!!, userUpdateDto)

        // then
        assertThat(userDto.username).isEqualTo(updateUsername)
        assertThat(userDto.loginId).isEqualTo(updateLoginId)
        assertThat(userDto.email).isEqualTo(updateEmail)
        assertThat(userDto.phoneNumber).isEqualTo(updatePhoneNumber)
    }

    @Test
    @DisplayName("회원 정보를 삭제할 수 있다.")
    fun deleteUserInfo() {
        // given
        val userId = caretaker.id
        val userType = UserType.valueOf(caretaker.javaClass.simpleName.uppercase())

        // when
        userService.deleteUser(userId!!, userType)

        // then
        assertThatThrownBy { userService.findUser(userId, userType) }
            .isExactlyInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage(ErrorCode.USER_NOT_FOUND.message)
    }

    private fun createCaretaker() {
        caretaker = Caretaker(
            username = "test-caretaker",
            loginId = "test-loginId",
            password = passwordEncoder.encode("test-password"),
            email = "test-email",
            phoneNumber = "test-phoneNumber"
        )
    }

    private fun createCaregiver() {
        caregiver = Caregiver(
            username = "test-caregiver",
            loginId = "test-loginId",
            password = passwordEncoder.encode("test-password"),
            email = "test-email",
            phoneNumber = "test-phoneNumber"
        )
    }
}