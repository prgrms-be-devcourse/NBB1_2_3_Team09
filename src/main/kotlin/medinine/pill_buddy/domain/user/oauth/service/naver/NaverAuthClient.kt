package medinine.pill_buddy.domain.user.oauth.service.naver

import medinine.pill_buddy.domain.user.oauth.dto.OAuthTokenResponse
import medinine.pill_buddy.global.config.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "naver-auth-client",
    url = "https://nid.naver.com",
    configuration = [FeignConfig::class]
)
interface NaverAuthClient {

    @PostMapping("/oauth2.0/token")
    fun getAccessToken(
        @RequestParam("grant_type") grantType: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("code") code: String
    ): OAuthTokenResponse
}