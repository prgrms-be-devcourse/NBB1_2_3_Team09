package medinine.pill_buddy.global.oauth

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
object KakaoProperty {

    @JvmStatic
    lateinit var KAKAO_CLIENT_ID: String
        private set

    @JvmStatic
    lateinit var KAKAO_CAREGIVER_REDIRECT_URI: String
        private set

    @JvmStatic
    lateinit var KAKAO_CARETAKER_REDIRECT_URI: String
        private set

    @JvmStatic
    lateinit var KAKAO_AUTHORIZATION_GRANT_TYPE: String
        private set

    @JvmStatic
    lateinit var KAKAO_AUTHORIZATION_URI: String
        private set

    @JvmStatic
    lateinit var KAKAO_TOKEN_URI: String
        private set

    @JvmStatic
    lateinit var KAKAO_USER_INFO_URI: String
        private set

    @JvmStatic
    val KAKAO_OAUTH_QUERY_STRING = "?response_type=code&client_id=%s&redirect_uri=%s"

    @Value("\${oauth.kakao.client-id}")
    fun setClientId(clientId: String) {
        KAKAO_CLIENT_ID = clientId
    }

    @Value("\${oauth.kakao.caregiver-redirect-uri}")
    fun setCaregiverRedirectUri(redirectUri: String) {
        KAKAO_CAREGIVER_REDIRECT_URI = redirectUri
    }

    @Value("\${oauth.kakao.caretaker-redirect-uri}")
    fun setCaretakerRedirectUri(redirectUri: String) {
        KAKAO_CARETAKER_REDIRECT_URI = redirectUri
    }

    @Value("\${oauth.kakao.authorization-grant-type}")
    fun setAuthorizationGrantType(authorizationGrantType: String) {
        KAKAO_AUTHORIZATION_GRANT_TYPE = authorizationGrantType
    }

    @Value("\${oauth.kakao.authorization-uri}")
    fun setAuthorizationUri(authorizationUri: String) {
        KAKAO_AUTHORIZATION_URI = authorizationUri
    }

    @Value("\${oauth.kakao.token-uri}")
    fun setTokenUri(tokenUri: String) {
        KAKAO_TOKEN_URI = tokenUri
    }

    @Value("\${oauth.kakao.user-info-uri}")
    fun setUserInfoUri(userInfoUri: String) {
        KAKAO_USER_INFO_URI = userInfoUri
    }
}