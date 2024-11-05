package medinine.pill_buddy.domain.user.oauth.service.naver

import medinine.pill_buddy.domain.user.oauth.dto.NaverUserResponse
import medinine.pill_buddy.global.config.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "naver-profile-client",
    url = "https://openapi.naver.com",
    configuration = [FeignConfig::class]
)
interface NaverProfileClient {

    @GetMapping("/v1/nid/me")
    fun getUserInfo(@RequestHeader("Authorization") accessToken: String): NaverUserResponse
}