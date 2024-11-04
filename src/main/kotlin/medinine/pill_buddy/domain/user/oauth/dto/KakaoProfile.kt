package medinine.pill_buddy.domain.user.oauth.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoProfile(

    @JsonProperty("id")
    val id : Long,

    @JsonProperty("properties")
    val properties : HashMap<String, String>,

    @JsonProperty("kakao_account")
    val kakaoAccount : KakaoAccount
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoAccount(
    @JsonProperty("email")
    val email : String
)