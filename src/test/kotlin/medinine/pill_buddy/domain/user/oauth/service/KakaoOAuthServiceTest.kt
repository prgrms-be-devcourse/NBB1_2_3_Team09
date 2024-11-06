//package medinine.pill_buddy.domain.user.oauth.service
//
//import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
//import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
//import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
//import medinine.pill_buddy.domain.user.dto.UserType
//import medinine.pill_buddy.domain.user.oauth.dto.KakaoAccount
//import medinine.pill_buddy.domain.user.oauth.dto.KakaoUserResponse
//import medinine.pill_buddy.domain.user.oauth.service.kakao.KakaoClient
//import medinine.pill_buddy.global.jwt.JwtToken
//import medinine.pill_buddy.global.jwt.JwtTokenProvider
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.Mockito.*
//import org.mockito.junit.jupiter.MockitoExtension
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.test.util.ReflectionTestUtils
//
//@ExtendWith(MockitoExtension::class)
//class KakaoOAuthServiceTest {
//
//    @InjectMocks
//    lateinit var kakaoOAuthService: KakaoOAuthService
//    @Mock
//    lateinit var jwtTokenProvider: JwtTokenProvider
//    @Mock
//    lateinit var kakaoClient: KakaoClient
//    @Mock
//    lateinit var caregiverRepository: CaregiverRepository
//    @Mock
//    lateinit var caretakerRepository: CaretakerRepository
//    @Mock
//    lateinit var passwordEncoder: PasswordEncoder
//
//    @Test
//    @DisplayName("소셜 계정을 처음 연동 시 회원 가입 후 토큰을 발급한다.")
//    fun login_when_user_does_not_exist() {
//        // given
//        val code = "test_code"
//        val userType = UserType.CAREGIVER
//        val accessToken = "accessToken"
//        val profile = KakaoUserResponse(
//            kakaoAccount = KakaoAccount(email = "test@example.com"),
//            properties = HashMap(mapOf("nickname" to "TestUser")),
//            id = 1
//        )
//        val caregiver = Caregiver(
//            username = "TestUser",
//            loginId = "kakao_1",
//            password = "encodedPassword",
//            email = "test@example.com"
//        )
//        val expected = JwtToken("Bearer", "access_token", "refresh_token")
//        ReflectionTestUtils.setField(kakaoOAuthService, "oauth2Password", "encodedPassword")
//
//        `when`(kakaoClient.getAccessToken(code)).thenReturn(accessToken)
//        `when`(kakaoClient.getUserInfo(accessToken)).thenReturn(profile)
//        `when`(caregiverRepository.existsByEmail("test@example.com")).thenReturn(false)
//        `when`(caregiverRepository.findByEmail("test@example.com")).thenReturn(caregiver)
//        `when`(passwordEncoder.encode(anyString())).thenReturn("encodedPassword")
//        `when`(jwtTokenProvider.generateToken("kakao_1")).thenReturn(expected)
//
//        // When
//        val result = kakaoOAuthService.login(code, userType)
//
//        // Then
//        verify(caregiverRepository).save(any(Caregiver::class.java))
//        assertThat(result).isEqualTo(expected)
//    }
//
//    @Test
//    @DisplayName("소셜 로그인 시 토큰을 발급한다.")
//    fun login_when_user_login() {
//        // given
//        val code = "test_code"
//        val userType = UserType.CAREGIVER
//        val accessToken = "accessToken"
//        val profile = KakaoUserResponse(
//            kakaoAccount = KakaoAccount(email = "test@example.com"),
//            properties = HashMap(mapOf("nickname" to "ExistingUser")),
//            id = 1
//        )
//        val caregiver = Caregiver(
//            username = "ExistingUser",
//            loginId = "kakao_1",
//            password = "encodedPassword",
//            email = "test@example.com"
//        )
//        val expected = JwtToken("Bearer", "access_token", "refresh_token")
//
//        ReflectionTestUtils.setField(kakaoOAuthService, "oauth2Password", "encodedPassword")
//        `when`(kakaoClient.getAccessToken(code)).thenReturn(accessToken)
//        `when`(kakaoClient.getUserInfo(accessToken)).thenReturn(profile)
//        `when`(caregiverRepository.existsByEmail("test@example.com")).thenReturn(true)
//        `when`(caregiverRepository.findByEmail("test@example.com")).thenReturn(caregiver)
//        `when`(jwtTokenProvider.generateToken("kakao_1")).thenReturn(expected)
//
//        // When
//        val result = kakaoOAuthService.login(code, userType)
//
//        // Then
//        verify(caregiverRepository, never()).save(any(Caregiver::class.java))
//        assertThat(result).isEqualTo(expected)
//    }
//}