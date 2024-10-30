package medinine.pill_buddy.domain.user.controller

import JoinDto
import medinine.pill_buddy.domain.user.dto.UserDto
import medinine.pill_buddy.domain.user.service.AuthService
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
    fun join(@Validated @RequestBody joinDto: JoinDto): ResponseEntity<UserDto> = ResponseEntity.ok(authService.join(joinDto))
}