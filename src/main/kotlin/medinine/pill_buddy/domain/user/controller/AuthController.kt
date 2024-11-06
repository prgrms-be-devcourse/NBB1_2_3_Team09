package medinine.pill_buddy.domain.user.controller

import JoinDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import medinine.pill_buddy.domain.user.dto.LoginDto
import medinine.pill_buddy.domain.user.dto.UserDto
import medinine.pill_buddy.domain.user.service.AuthService
import medinine.pill_buddy.global.jwt.JwtToken
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/users")
@Tag(name = "회원 인증 기능", description = "서비스 이용을 위한 회원가입, 로그인, 로그아웃 기능을 제공한다.")
class AuthController(
    private val authService: AuthService
) {
    @Operation(description = "사용자는 회원가입을 할 수 있다.")
    @PostMapping("/join")
    fun join(@Validated @RequestBody joinDto: JoinDto): ResponseEntity<UserDto> =
        ResponseEntity.ok(authService.join(joinDto))

    @Operation(description = "사용자는 로그인을 할 수 있다.")
    @PostMapping("/login")
    fun login(@Validated @RequestBody loginDto: LoginDto): ResponseEntity<JwtToken> =
        ResponseEntity.ok(authService.login(loginDto))

    @Operation(description = "사용자는 로그아웃을 할 수 있다.")
    @PostMapping("/logout")
    fun logout(): ResponseEntity<Void> {
        authService.logout()

        return ResponseEntity.noContent().build()
    }

    @Operation(description = "사용자는 만료된 Jwt 토큰을 재발급할 수 있다.")
    @PostMapping("/reissue-token")
    fun reissueToken(@RequestHeader(HttpHeaders.AUTHORIZATION) bearerToken: String?): ResponseEntity<JwtToken> =
        ResponseEntity.ok(authService.reissueToken(bearerToken))
}