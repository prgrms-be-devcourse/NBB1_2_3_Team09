package medinine.pill_buddy.domain.user.oauth.controller

import lombok.RequiredArgsConstructor
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.oauth.service.SocialLoginService
import medinine.pill_buddy.global.jwt.JwtToken
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.net.URI


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users/oauth")
class OAuthController(
    private val socialLoginService: SocialLoginService
) {

    @GetMapping("/connection/kakao")
    fun getConnectionByKakao(@RequestParam userType: UserType): ResponseEntity<String> {
        val location: String = socialLoginService.getConnectionUrl(userType, "kakao")

        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(location))
            .build()
    }

    @GetMapping("/connection/naver")
    fun getConnectionByNaver(@RequestParam userType: UserType): ResponseEntity<String> {
        val location: String = socialLoginService.getConnectionUrl(userType, "naver")

        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(location))
            .build()
    }

    @GetMapping("/login/{registrationId}/{userType}")
    fun login(
        @RequestParam code: String,
        @PathVariable userType: String,
        @PathVariable registrationId: String
    ): ResponseEntity<JwtToken> {
        val jwtToken: JwtToken = socialLoginService.login(code, UserType.from(userType), registrationId)

        return ResponseEntity.ok(jwtToken)
    }
}