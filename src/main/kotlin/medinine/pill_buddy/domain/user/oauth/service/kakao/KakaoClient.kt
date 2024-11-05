package medinine.pill_buddy.domain.user.oauth.service.kakao

import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.dto.UserType.*
import medinine.pill_buddy.domain.user.oauth.dto.KakaoUserResponse
import medinine.pill_buddy.domain.user.oauth.dto.OAuthProfile
import medinine.pill_buddy.domain.user.oauth.service.OAuthClient
import medinine.pill_buddy.domain.user.oauth.constant.KakaoProperty.KAKAO_AUTHORIZATION_GRANT_TYPE
import medinine.pill_buddy.domain.user.oauth.constant.KakaoProperty.KAKAO_CAREGIVER_REDIRECT_URI
import medinine.pill_buddy.domain.user.oauth.constant.KakaoProperty.KAKAO_CARETAKER_REDIRECT_URI
import medinine.pill_buddy.domain.user.oauth.constant.KakaoProperty.KAKAO_CLIENT_ID
import org.springframework.stereotype.Component

@Component
class KakaoClient(
    private val kakaoAuthClient: KakaoAuthClient,
    private val kakaoProfileClient: KakaoProfileClient
) : OAuthClient {

    companion object {
        private const val KAKAO_AUTHORIZATION_URI = "https://kauth.kakao.com/oauth/authorize"
        private const val KAKAO_AUTH_QUERY_STRING = "?response_type=code&client_id=%s&redirect_uri=%s"
    }

    override fun getConnectionUrl(userType: UserType): String {
        return when (userType) {
            CAREGIVER -> KAKAO_AUTHORIZATION_URI + String.format(KAKAO_AUTH_QUERY_STRING, KAKAO_CLIENT_ID, KAKAO_CAREGIVER_REDIRECT_URI)
            CARETAKER -> KAKAO_AUTHORIZATION_URI + String.format(KAKAO_AUTH_QUERY_STRING, KAKAO_CLIENT_ID, KAKAO_CARETAKER_REDIRECT_URI)
        }
    }

    override fun getUserInfo(code: String): OAuthProfile {
        val tokenResponse = kakaoAuthClient.getAccessToken(KAKAO_AUTHORIZATION_GRANT_TYPE, KAKAO_CLIENT_ID, code)
        val userInfo = kakaoProfileClient.getUserInfo("Bearer ${tokenResponse.accessToken}")

        return getOAuthProfile(userInfo)
    }

    private fun getOAuthProfile(userInfo: KakaoUserResponse): OAuthProfile {
        return OAuthProfile(
            id = userInfo.id.toString(),
            nickname = userInfo.properties["nickname"] ?: throw IllegalArgumentException(),
            email = userInfo.kakaoAccount.email
        )
    }
}