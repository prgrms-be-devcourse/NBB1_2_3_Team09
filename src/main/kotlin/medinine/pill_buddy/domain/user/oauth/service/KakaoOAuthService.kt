package medinine.pill_buddy.domain.user.oauth.service

import lombok.RequiredArgsConstructor
import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.dto.UserType.CAREGIVER
import medinine.pill_buddy.domain.user.dto.UserType.CARETAKER
import medinine.pill_buddy.domain.user.oauth.dto.KakaoProfile
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.global.jwt.JwtToken
import medinine.pill_buddy.global.jwt.JwtTokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
@RequiredArgsConstructor
class KakaoOAuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val kakaoClient: KakaoClient,
    private val caretakerRepository: CaretakerRepository,
    private val caregiverRepository: CaregiverRepository,
    private val passwordEncoder: PasswordEncoder
) : OAuthService {

    @Value("\${oauth.kakao.kakao-password}")
    private lateinit var oauth2Password: String

    override fun getConnectionUrl(userType: UserType): String {
        return kakaoClient.getConnectionUrl(userType)
    }

    override fun login(code: String, userType: UserType): JwtToken {
        // code 를 통해 카카오 서버로 Token 요청 (POST 요청)
        val accessToken: String = kakaoClient.getAccessToken(code)

        // Token 을 통해 카카오 서버로 사용자 데이터 요청 (POST 요청)
        val profile: KakaoProfile = kakaoClient.getUserInfo(accessToken)
        val email: String = profile.kakaoAccount.email

        // 존재하지 않는 회원일 경우 회원 가입
        if (!isEmailExists(email, userType)) {
            userAppend(profile, userType)
        }

        // 이메일을 통해 회원 조회 후, Jwt 토큰 반환
        if (userType == CAREGIVER) {
            val caregiver: Caregiver = caregiverRepository.findByEmail(email)!!
            return jwtTokenProvider.generateToken(caregiver.loginId)
        } else {
            val caretaker: Caretaker = caretakerRepository.findByEmail(email)!!
            return jwtTokenProvider.generateToken(caretaker.loginId)
        }
    }

    private fun userAppend(profile: KakaoProfile, userType: UserType) {
        val loginId = "kakao_${profile.id}"
        val encodedPassword = passwordEncoder.encode(oauth2Password)
        val username: String = profile.properties["nickname"] ?: "user_${profile.id}"

        when (userType) {
            CARETAKER -> {
                val caretaker = Caretaker(
                    username = username,
                    loginId = loginId,
                    password = encodedPassword,
                    email = profile.kakaoAccount.email
                )
                caretakerRepository.save(caretaker)
            }

            CAREGIVER -> {
                val caregiver = Caregiver(
                    username = username,
                    loginId = loginId,
                    password = encodedPassword,
                    email = profile.kakaoAccount.email
                )
                caregiverRepository.save(caregiver)
            }
        }
    }

    private fun isEmailExists(email: String, userType: UserType): Boolean {
        return when(userType) {
            CARETAKER -> caretakerRepository.existsByEmail(email)
            CAREGIVER -> caregiverRepository.existsByEmail(email)
        }
    }
}