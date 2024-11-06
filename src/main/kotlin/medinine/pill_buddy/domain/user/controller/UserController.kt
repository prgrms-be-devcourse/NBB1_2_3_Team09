package medinine.pill_buddy.domain.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import medinine.pill_buddy.domain.user.dto.UserDto
import medinine.pill_buddy.domain.user.dto.UserPasswordUpdateDto
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.dto.UserUpdateDto
import medinine.pill_buddy.domain.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/users")
@Tag(name = "회원정보 관련 기능", description = "회원정보 수정, 조회 및 회원탈퇴 기능을 제공한다.")
class UserController(
    private val userService: UserService
) {
    @Operation(description = "사용자는 자신의 회원정보를 조회할 수 있다.")
    @GetMapping("/{userId}")
    fun findUserInfo(
        @PathVariable userId: Long,
        @RequestParam userType: UserType
    ): ResponseEntity<UserDto> {
        val user = userService.findUser(userId, userType)
        return ResponseEntity.ok(user)
    }

    @Operation(description = "사용자는 회원정보를 수정할 수 있다.")
    @PutMapping("/{userId}")
    fun updateUserInfo(
        @PathVariable userId: Long,
        @RequestBody(required = true) @Validated userUpdateDto: UserUpdateDto
    ): ResponseEntity<UserDto> {
        val updatedUser = userService.updateUserInfo(userId, userUpdateDto)
        return ResponseEntity.ok(updatedUser)
    }

    @Operation(description = "사용자는 비밀번호를 수정할 수 있다.")
    @PutMapping("/{userId}/password")
    fun updateUserPassword(
        @PathVariable userId: Long,
        @RequestBody(required = true) @Validated userPasswordUpdateDto: UserPasswordUpdateDto
    ): ResponseEntity<UserDto> {
        val updatedPasswordUser = userService.updateUserPassword(userId, userPasswordUpdateDto)
        return ResponseEntity.ok(updatedPasswordUser)
    }

    @Operation(description = "사용자는 회원 탈퇴를 할 수 있다.")
    @DeleteMapping("/{userId}")
    fun deleteUser(
        @PathVariable userId: Long,
        @RequestParam userType: UserType
    ): ResponseEntity<Void> {
        userService.deleteUser(userId, userType)
        return ResponseEntity.noContent().build()
    }
}