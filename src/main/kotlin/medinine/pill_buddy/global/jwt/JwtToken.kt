package medinine.pill_buddy.global.jwt

class JwtToken(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
)