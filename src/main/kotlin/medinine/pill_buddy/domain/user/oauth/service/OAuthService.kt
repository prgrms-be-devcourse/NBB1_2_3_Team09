package medinine.pill_buddy.domain.user.oauth.service

import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.global.jwt.JwtToken


interface OAuthService {
    fun getConnectionUrl(userType: UserType): String
    fun login(code: String, userType: UserType): JwtToken
}