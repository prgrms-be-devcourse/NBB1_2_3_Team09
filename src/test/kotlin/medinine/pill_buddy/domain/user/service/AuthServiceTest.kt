package medinine.pill_buddy.domain.user.service

import JoinDto
import jakarta.transaction.Transactional
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.user.dto.LoginDto
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.global.jwt.JwtTokenProvider
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
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
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

    @Test
    @DisplayName("LoginDto 를 통해 로그인을 할 수 있다.")
    fun login() {
        // given
        val loginDto = LoginDto(caretaker.loginId, "test-password")

        // when
        val jwtToken = authService.login(loginDto)
        val authentication = jwtTokenProvider.getAuthenticationByToken(jwtToken.accessToken)

        // then
        assertThat(jwtToken.grantType).isEqualTo("Bearer")
        assertThat(authentication.authorities.iterator().next().authority).isEqualTo("ROLE_USER")
        assertThat(authentication.name).isEqualTo(loginDto.loginId)
    }

    @Test
    @DisplayName("LoginId 를 찾을 수 없다면, PillBuddyCustomException 이 발생한다.")
    fun loginWithInvalidLoginId() {
        val invalidLoginId = "invalidLoginId"
        val password = "password3"
        val loginDto = LoginDto(invalidLoginId, password)

        assertThatThrownBy { authService.login(loginDto) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage(ErrorCode.USER_NOT_FOUND.message)
    }

    @Test
    @DisplayName("password 가 저장된 비밀번호와 일치하지 않다면, PillBuddyCustomException 이 발생한다.")
    fun loginWithInvalidPassword() {
        val loginId = caretaker.loginId
        val invalidPassword = "invalidPassword"
        val loginDto = LoginDto(loginId, invalidPassword)

        assertThatThrownBy { authService.login(loginDto) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage(ErrorCode.USER_MISMATCHED_ID_OR_PASSWORD.message)
    }

    @Test
    @DisplayName("사용자는 refreshToken 을 통해 토큰을 재발급 할 수 있다.")
    fun reissueToken() {
        // given
        val loginId = caretaker.loginId
        val password = "test-password"
        val loginDto = LoginDto(loginId, password)

        val jwtToken = authService.login(loginDto)
        val refreshToken = "Bearer " + jwtToken.refreshToken

        // when
        val reissuedJwtToken = authService.reissueToken(refreshToken)
        val reissuedAccessToken = reissuedJwtToken.accessToken
        val authentication = jwtTokenProvider.getAuthenticationByToken(reissuedAccessToken)

        // then
        assertThat(authentication.name).isEqualTo(loginId)
        assertThat(authentication.authorities.iterator().next().authority).isEqualTo("ROLE_USER")
    }

    @Test
    @DisplayName("유효하지 않은 refreshToken 이면, PillBuddyException 이 발생한다.")
    fun reissueToken_with_expired_refreshToken() {
        // given
        val malformedRefreshToken = "Bearer malformed_jwt_token"

        assertThatThrownBy { authService.reissueToken(malformedRefreshToken) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage("유효하지 않은 JWT 토큰입니다.")
    }

    @Test
    @DisplayName("GrantType 이 없는 refreshToken 이면, PillBuddyException 이 발생한다.")
    fun reissueToken_without_grantType() {
        // given
        val refreshToken = "simple_refresh_token"

        assertThatThrownBy { authService.reissueToken(refreshToken) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage("유효하지 않은 JWT 토큰입니다.")
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