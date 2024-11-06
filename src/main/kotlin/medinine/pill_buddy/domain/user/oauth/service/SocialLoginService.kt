package medinine.pill_buddy.domain.user.oauth.service

import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.global.jwt.JwtToken
import medinine.pill_buddy.global.jwt.JwtTokenProvider
import org.springframework.stereotype.Service

@Service
class SocialLoginService(
    private val oAuthClientMap: Map<String, OAuthClient>,
    private val userReader: UserReader,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun getConnectionUrl(userType: UserType, registrationId: String): String {
        val oAuthClient = oAuthClientMap["${registrationId}Client"]!!
        return oAuthClient.getConnectionUrl(userType)
    }

    fun login(code: String, userType: UserType, registrationId: String): JwtToken {
        val oAuthClient = oAuthClientMap["${registrationId}Client"]!!
        val userInfo = oAuthClient.getUserInfo(code)

        if (!userReader.isNewUser(userInfo.email, userType)) {
            userReader.registerUser(userInfo, userType, registrationId)
        }

        val loginID = userReader.getUserLoginID(userInfo.email, userType)
        return jwtTokenProvider.generateToken(loginID)
    }
}