package medinine.pill_buddy.domain.user.oauth.service

import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.oauth.dto.OAuthProfile

interface OAuthClient {

    fun getConnectionUrl(userType: UserType): String
    fun getUserInfo(code: String): OAuthProfile
}