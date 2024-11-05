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

    @Value("\${oauth.authorization-grant-type}")
    fun setAuthorizationGrantType(authorizationGrantType: String) {
        KAKAO_AUTHORIZATION_GRANT_TYPE = authorizationGrantType
    }
}