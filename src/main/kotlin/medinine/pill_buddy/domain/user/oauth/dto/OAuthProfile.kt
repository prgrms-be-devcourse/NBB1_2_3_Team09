package medinine.pill_buddy.domain.user.oauth.dto

data class OAuthProfile(
    val id : String,
    val nickname : String,
    val email : String,
    val phoneNumber : String? = null
)