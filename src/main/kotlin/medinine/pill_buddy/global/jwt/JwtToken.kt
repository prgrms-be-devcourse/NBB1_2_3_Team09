package medinine.pill_buddy.global.jwt

class JwtToken(
    private val grantType: String,
    private val accessToken: String,
    private val refreshToken: String,
)