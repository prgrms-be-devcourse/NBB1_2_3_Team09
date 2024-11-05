package medinine.pill_buddy.domain.user.oauth.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class NaverUserResponse(

    @JsonProperty("response")
    val naverUserDetail: NaverUserDetail
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NaverUserDetail(

    @JsonProperty("id")
    val id: String,

    @JsonProperty("nickname")
    val nickname: String,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("mobile")
    val phoneNumber: String
)