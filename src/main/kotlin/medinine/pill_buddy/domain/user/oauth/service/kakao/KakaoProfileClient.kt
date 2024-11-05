package medinine.pill_buddy.domain.user.oauth.service.kakao

import medinine.pill_buddy.domain.user.oauth.dto.KakaoUserResponse
import medinine.pill_buddy.global.config.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "kakao-profile-client",
    url = "https://kapi.kakao.com",
    configuration = [FeignConfig::class]
)
interface KakaoProfileClient {

    @GetMapping("/v2/user/me")
    fun getUserInfo(@RequestHeader("Authorization") accessToken: String): KakaoUserResponse
}