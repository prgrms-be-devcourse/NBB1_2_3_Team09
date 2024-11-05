package medinine.pill_buddy.global.oauth

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
object NaverProperty {

    @JvmStatic
    lateinit var NAVER_CLIENT_ID: String
        private set

    @JvmStatic
    lateinit var NAVER_CLIENT_SECRET: String
        private set

    @JvmStatic
    lateinit var NAVER_CAREGIVER_REDIRECT_URI: String
        private set

    @JvmStatic
    lateinit var NAVER_CARETAKER_REDIRECT_URI: String
        private set

    @JvmStatic
    lateinit var NAVER_AUTHORIZATION_GRANT_TYPE: String
        private set

    @Value("\${oauth.naver.client-id}")
    fun setNaverClientId(naverClientId: String) {
        NAVER_CLIENT_ID = naverClientId
    }

    @Value("\${oauth.naver.client-secret}")
    fun setNaverClientSecret(naverClientSecret: String) {
        NAVER_CLIENT_SECRET = naverClientSecret
    }

    @Value("\${oauth.naver.caregiver-redirect-uri}")
    fun setNaverCaregiverRedirectUri(naverCaregiverRedirectUri: String) {
        NAVER_CAREGIVER_REDIRECT_URI = naverCaregiverRedirectUri
    }

    @Value("\${oauth.naver.caretaker-redirect-uri}")
    fun setNaverCaretakerRedirectUri(naverCaretakerRedirectUri: String) {
        NAVER_CARETAKER_REDIRECT_URI = naverCaretakerRedirectUri
    }

    @Value("\${oauth.authorization-grant-type}")
    fun setNaverAuthorizationGrantType(naverAuthorizationGrantType: String) {
        NAVER_AUTHORIZATION_GRANT_TYPE = naverAuthorizationGrantType
    }
}