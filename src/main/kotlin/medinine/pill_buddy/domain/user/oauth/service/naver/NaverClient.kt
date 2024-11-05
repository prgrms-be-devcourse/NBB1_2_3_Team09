package medinine.pill_buddy.domain.user.oauth.service.naver

import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.dto.UserType.CAREGIVER
import medinine.pill_buddy.domain.user.dto.UserType.CARETAKER
import medinine.pill_buddy.domain.user.oauth.dto.NaverUserResponse
import medinine.pill_buddy.domain.user.oauth.dto.OAuthProfile
import medinine.pill_buddy.domain.user.oauth.service.OAuthClient
import medinine.pill_buddy.domain.user.oauth.constant.NaverProperty.NAVER_AUTHORIZATION_GRANT_TYPE
import medinine.pill_buddy.domain.user.oauth.constant.NaverProperty.NAVER_CAREGIVER_REDIRECT_URI
import medinine.pill_buddy.domain.user.oauth.constant.NaverProperty.NAVER_CARETAKER_REDIRECT_URI
import medinine.pill_buddy.domain.user.oauth.constant.NaverProperty.NAVER_CLIENT_ID
import medinine.pill_buddy.domain.user.oauth.constant.NaverProperty.NAVER_CLIENT_SECRET
import org.springframework.stereotype.Component

@Component
class NaverClient(
    private val naverAuthClient: NaverAuthClient,
    private val naverProfileClient: NaverProfileClient
) : OAuthClient {

    companion object {
        private const val NAVER_OAUTH_QUERY_STRING =
            "/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s"
        private const val NAVER_AUTHORIZATION_URI = "https://nid.naver.com"
    }

    override fun getConnectionUrl(userType: UserType): String {
        return when (userType) {
            CAREGIVER -> NAVER_AUTHORIZATION_URI + String.format(
                NAVER_OAUTH_QUERY_STRING, NAVER_CLIENT_ID, NAVER_CAREGIVER_REDIRECT_URI
            )

            CARETAKER -> NAVER_AUTHORIZATION_URI + String.format(
                NAVER_OAUTH_QUERY_STRING, NAVER_CLIENT_ID, NAVER_CARETAKER_REDIRECT_URI
            )
        }
    }

    override fun getUserInfo(code: String): OAuthProfile {
        val tokenResponse = naverAuthClient.getAccessToken(
            NAVER_AUTHORIZATION_GRANT_TYPE,
            NAVER_CLIENT_ID,
            NAVER_CLIENT_SECRET,
            code
        )
        val userInfo = naverProfileClient.getUserInfo("Bearer ${tokenResponse.accessToken}")

        return getOAuthProfile(userInfo)
    }

    private fun getOAuthProfile(userInfo: NaverUserResponse): OAuthProfile {
        val naverUserDetail = userInfo.naverUserDetail

        return OAuthProfile(
            id = naverUserDetail.id.substring(0, minOf(naverUserDetail.id.length, 10)),
            nickname = naverUserDetail.nickname,
            email = naverUserDetail.email,
            phoneNumber = naverUserDetail.phoneNumber
        )
    }
}