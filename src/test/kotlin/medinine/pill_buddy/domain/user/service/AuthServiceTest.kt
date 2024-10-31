package medinine.pill_buddy.domain.user.service

import JoinDto
import jakarta.transaction.Transactional
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.user.dto.UserType
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

@SpringBootTest
@Transactional
class AuthServiceTest @Autowired constructor(
    private val authService: AuthService,
    private val caretakerRepository: CaretakerRepository,
    private val passwordEncoder: PasswordEncoder
) {
    private lateinit var caretaker: Caretaker

    @BeforeEach
    fun setup() {
        createCaretaker()
        caretakerRepository.save(caretaker)
    }

    @Test
    @DisplayName("JoinDto 를 통해 회원가입을 할 수 있다.")
    fun join() {
        // given
        val joinDto = JoinDto(
            username = "newUser",
            loginId = "newLoginId",
            password = "newPassword",
            email = "new@gmail.com",
            phoneNumber = "010-1112-2221",
            userType = UserType.CARETAKER
        )

        // when
        val userDto = authService.join(joinDto)

        // then
        assertThat(userDto.username).isEqualTo("newUser")
        assertThat(userDto.phoneNumber).isEqualTo("010-1112-2221")
        assertThat(userDto.email).isEqualTo("new@gmail.com")
        assertThat(userDto.userType).isEqualTo("caretaker")
    }

    @Test
    @DisplayName("JoinDto 를 통해 회원가입 시 중복된 이메일을 입력하면 예외가 발생한다.")
    fun join_with_duplicated_email_exception() {
        val duplicatedEmail = "test-email"

        val joinDto = JoinDto(
            username = "newUser",
            loginId = "newLoginId",
            password = "newPassword",
            email = duplicatedEmail,
            phoneNumber = "010-1112-2222",
            userType = UserType.CARETAKER
        )

        assertThatThrownBy { authService.join(joinDto) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage(ErrorCode.USER_ALREADY_REGISTERED_EMAIL.message)
    }

    @Test
    @DisplayName("JoinDto 를 통해 회원가입 시 중복된 로그인 아이디를 입력하면 예외가 발생한다.")
    fun join_with_duplicated_login_id_exception() {
        val duplicatedLoginId = "test-loginId"

        val joinDto = JoinDto(
            username = "newUser",
            loginId = duplicatedLoginId,
            password = "newPassword",
            email = "new@gmail.com",
            phoneNumber = "010-1112-2223",
            userType = UserType.CARETAKER
        )

        assertThatThrownBy { authService.join(joinDto) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage(ErrorCode.USER_ALREADY_REGISTERED_LOGIN_ID.message)
    }

    @Test
    @DisplayName("JoinDto 를 통해 회원가입 시 중복된 전화번호를 입력하면 예외가 발생한다.")
    fun join_with_duplicated_phoneNumber_exception() {
        val duplicatedPhoneNumber = "test-phoneNumber"

        val joinDto = JoinDto(
            username = "newUser",
            loginId = "newLoginId",
            password = "newPassword",
            email = "new@gmail.com",
            phoneNumber = duplicatedPhoneNumber,
            userType = UserType.CARETAKER
        )

        assertThatThrownBy { authService.join(joinDto) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage(ErrorCode.USER_ALREADY_REGISTERED_PHONE_NUMBER.message)
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
}