package medinine.pill_buddy.domain.user.oauth.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoTokenResponse(

    @JsonProperty("token_type")
    val tokenType : String,

    @JsonProperty("access_token")
    val accessToken : String,

    @JsonProperty("refresh_token")
    val refreshToken : String
)