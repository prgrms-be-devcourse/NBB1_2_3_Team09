package medinine.pill_buddy.domain.user.oauth.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.oauth.service.SocialLoginService
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
@Tag(name = "소셜 로그인 기능", description = "사용자는 카카오 혹은 네이버 소셜 계정을 통해 로그인 및 회원가입을 할 수 있다.")
class OAuthController(
    private val socialLoginService: SocialLoginService
) {

    @Operation(description = "사용자는 카카오 소셜 로그인을 할 수 있다.")
    @GetMapping("/connection/kakao")
    fun getConnectionByKakao(@RequestParam userType: UserType): ResponseEntity<String> {
        val location: String = socialLoginService.getConnectionUrl(userType, "kakao")

        return ResponseEntity.ok(location)
    }

    @Operation(description = "사용자는 네이버 소셜 로그인을 할 수 있다.")
    @GetMapping("/connection/naver")
    fun getConnectionByNaver(@RequestParam userType: UserType): ResponseEntity<String> {
        val location: String = socialLoginService.getConnectionUrl(userType, "naver")

        return ResponseEntity.ok(location)
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