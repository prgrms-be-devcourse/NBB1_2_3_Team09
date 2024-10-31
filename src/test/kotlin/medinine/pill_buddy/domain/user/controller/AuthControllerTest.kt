package medinine.pill_buddy.domain.user.controller

import JoinDto
import com.fasterxml.jackson.databind.ObjectMapper
import medinine.pill_buddy.domain.user.dto.LoginDto
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.service.AuthService
import medinine.pill_buddy.global.jwt.JwtToken
import medinine.pill_buddy.global.redis.RedisUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest @Autowired constructor(
    private val redisUtils: RedisUtils,
    private val authService: AuthService,
    private val mapper: ObjectMapper,
    private val mvc: MockMvc
) {
    @BeforeEach
    fun setup() {
        authService.join(
            JoinDto(
            username = "tester",
            loginId = "testLoginId",
            password = "testPassword123!@",
            email = "test@gmail.com",
            phoneNumber = "01011110000",
            userType = UserType.CARETAKER
        ))
    }

    @AfterEach
    fun clearAll() {
        redisUtils.clearAll()
    }

    @Test
    @DisplayName("회원가입 테스트")
    fun joinTest() {
        // given
        val username = "newUser"
        val loginId = "newLoginId"
        val password = "newPassword123!@"
        val email = "new@gmail.com"
        val phoneNumber = "01011122221"
        val userType = "CARETAKER"

        // when
        val body = mapper.writeValueAsString(
            JoinDto(
                username = username,
                loginId = loginId,
                password = password,
                email = email,
                phoneNumber = phoneNumber,
                userType = UserType.valueOf(userType)
            )
        )

        // then
        mvc.perform(post("/api/users/join")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(
        """
            {
                "username": "$username",
                "loginId": "$loginId",
                "email": "$email",
                "phoneNumber": "$phoneNumber",
                "userType": "${userType.lowercase()}"
            }
        """.trimIndent()))
    }

    @Test
    @DisplayName("회원 로그인 테스트")
    fun login() {
        // given
        val loginId = "testLoginId"
        val password = "testPassword123!@"
        val body = mapper.writeValueAsString(LoginDto(loginId = loginId, password = password))

        // when
        val response = mvc.perform(
            post("/api/users/login")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // then
        val jsonResponse = response.response.contentAsString
        val jwtTokenResponse = mapper.readValue(jsonResponse, JwtToken::class.java)

        assertThat(jwtTokenResponse.grantType).isEqualTo("Bearer")
        assertThat(jwtTokenResponse.accessToken).isNotEmpty
    }

    @Test
    @DisplayName("Jwt 토큰 재발급 테스트")
    fun reissueToken() {
        // given
        val loginDto = LoginDto(loginId = "testLoginId", password = "testPassword123!@")
        val jwtToken = authService.login(loginDto)
        Thread.sleep(1000)

        // when
        val response = mvc.perform(post("/api/users/reissue-token")
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${jwtToken.refreshToken}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn()

        // then
        val jsonResponse = response.response.contentAsString
        val jwtTokenResponse = mapper.readValue(jsonResponse, JwtToken::class.java)

        assertThat(jwtTokenResponse.grantType).isEqualTo("Bearer")
        assertThat(jwtTokenResponse.accessToken).isNotEqualTo(jwtToken.accessToken)
        assertThat(jwtTokenResponse.refreshToken).isNotEqualTo(jwtToken.refreshToken)
    }
}