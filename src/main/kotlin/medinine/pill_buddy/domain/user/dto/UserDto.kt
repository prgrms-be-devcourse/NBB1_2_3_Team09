package medinine.pill_buddy.domain.user.dto

import medinine.pill_buddy.domain.user.entity.User

data class UserDto(
    val userType: String,
    val username: String,
    val loginId: String,
    val email: String,
    val phoneNumber: String?,
    val imageUrl: String
) {
    constructor(user: User) : this(
        userType = user::class.simpleName!!.lowercase(),
        username = user.username,
        loginId = user.loginId,
        email = user.email,
        phoneNumber = user.phoneNumber,
        imageUrl = user.image?.url ?: "default.jpg"
    )
}