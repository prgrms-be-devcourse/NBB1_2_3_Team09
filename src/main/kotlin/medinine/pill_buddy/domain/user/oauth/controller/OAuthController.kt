package medinine.pill_buddy.domain.user.oauth.controller

import lombok.RequiredArgsConstructor
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.oauth.service.KakaoOAuthService
import medinine.pill_buddy.global.jwt.JwtToken
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users/oauth")
class OAuthController(
    private val kakaoOAuthService: KakaoOAuthService
) {

    @GetMapping("/connection")
    fun loginPage(@RequestParam userType: UserType): ResponseEntity<String> {
        val location: String = kakaoOAuthService.getConnectionUrl(userType)

        return ResponseEntity.ok(location)
    }

    @GetMapping("/login/{userType}")
    fun login(@RequestParam code: String, @PathVariable userType: String): ResponseEntity<JwtToken> {
        val jwtToken: JwtToken = kakaoOAuthService.login(code, UserType.from(userType))

        return ResponseEntity.ok(jwtToken)
    }
}