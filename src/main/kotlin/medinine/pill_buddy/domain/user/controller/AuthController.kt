package medinine.pill_buddy.domain.user.controller

import JoinDto
import medinine.pill_buddy.domain.user.dto.LoginDto
import medinine.pill_buddy.domain.user.dto.UserDto
import medinine.pill_buddy.domain.user.service.AuthService
import medinine.pill_buddy.global.jwt.JwtToken
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/users")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/join")
    fun join(@Validated @RequestBody joinDto: JoinDto): ResponseEntity<UserDto> =
        ResponseEntity.ok(authService.join(joinDto))

    @PostMapping("/login")
    fun login(@Validated @RequestBody loginDto: LoginDto): ResponseEntity<JwtToken> =
        ResponseEntity.ok(authService.login(loginDto))

    @PostMapping("/logout")
    fun logout(): ResponseEntity<Void> {
        authService.logout()

        return ResponseEntity.noContent().build()
    }
}