package medinine.pill_buddy.global.jwt

import jakarta.transaction.Transactional
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.global.redis.RedisUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class JwtTokenProviderTest(
    @Autowired
    private var jwtTokenProvider: JwtTokenProvider,

    @Autowired
    private val caretakerRepository: CaretakerRepository,

    @Autowired
    private var redisUtils: RedisUtils
) {
    private lateinit var caretaker: Caretaker

    @BeforeEach
    fun setup() {
        createCaretaker()
        caretakerRepository.save(caretaker)
    }

    @AfterEach
    fun clear() {
        redisUtils.clearAll()
    }

    @Test
    @DisplayName("정상적인 Jwt 토큰을 생성하여야 한다.")
    fun generateToken() {
        // given
        val loginId = caretaker.loginId

        // when
        val jwtToken = jwtTokenProvider.generateToken(loginId)

        // then
        assertThat(jwtToken).isNotNull
        assertThat(jwtToken.grantType).isEqualTo("Bearer")
        assertThat(jwtToken.accessToken).isNotBlank
        assertThat(jwtToken.refreshToken).isNotBlank
    }

    @Test
    @DisplayName("refreshToken 을 통해 새로운 Jwt 토큰을 발급할 수 있어야 한다.")
    fun reissueAccessToken() {
        // given
        val loginId = caretaker.loginId

        // when
        // jwtTokenProvider.generateToken(loginId) 통해 Jwt 토큰을 발급받고, 재발급을 위한 refreshToken 을 저장
        val jwtToken = jwtTokenProvider.generateToken(loginId)

        // 발급 시간, 유효 기간을 다르게 하기 위해 1초 대기
        Thread.sleep(1000)

        val newToken = jwtTokenProvider.reissueAccessToken(jwtToken.refreshToken)

        assertThat(newToken).isNotNull
        assertThat(newToken.accessToken).isNotEqualTo(jwtToken.accessToken)
        assertThat(newToken.accessToken).isNotEqualTo(jwtToken.refreshToken)
    }

    @Test
    @DisplayName("토큰을 통해 사용자 정보를 가져올 수 있다.")
    fun getAuthenticationByToken() {
        // given
        val loginId = caretaker.loginId
        val jwtToken = jwtTokenProvider.generateToken(loginId)

        // when
        val authentication = jwtTokenProvider.getAuthenticationByToken(jwtToken.accessToken)

        // then
        assertThat(authentication).isNotNull
        assertThat(authentication.authorities.iterator().next().authority).isEqualTo("ROLE_USER")
    }

    private fun createCaretaker() {
        caretaker = Caretaker(
            username = "test-caretaker",
            loginId = "test-loginId",
            password = "test-password",
            email = "test-email",
            phoneNumber = "test-phoneNumber"
        )
    }
}