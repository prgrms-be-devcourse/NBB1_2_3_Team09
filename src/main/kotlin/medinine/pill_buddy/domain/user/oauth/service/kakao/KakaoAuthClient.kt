package medinine.pill_buddy.domain.user.oauth.service.kakao

import medinine.pill_buddy.domain.user.oauth.dto.OAuthTokenResponse
import medinine.pill_buddy.global.config.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "kakao-auth-client",
    url = "https://kauth.kakao.com",
    configuration = [FeignConfig::class]
)
interface KakaoAuthClient {

    @PostMapping("/oauth/token")
    fun getAccessToken(
        @RequestParam("grant_type") grantType: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("code") code: String
    ): OAuthTokenResponse
}