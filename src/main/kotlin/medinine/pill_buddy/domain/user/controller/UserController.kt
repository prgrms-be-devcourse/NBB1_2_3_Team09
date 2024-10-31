package medinine.pill_buddy.domain.user.controller

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
class UserController(
    private val userService: UserService
) {
    @GetMapping("/{userId}")
    fun findUserInfo(
        @PathVariable userId: Long,
        @RequestParam userType: UserType
    ): ResponseEntity<UserDto> {
        val user = userService.findUser(userId, userType)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{userId}")
    fun updateUserInfo(
        @PathVariable userId: Long,
        @RequestBody(required = true) @Validated userUpdateDto: UserUpdateDto
    ): ResponseEntity<UserDto> {
        val updatedUser = userService.updateUserInfo(userId, userUpdateDto)
        return ResponseEntity.ok(updatedUser)
    }

    @PutMapping("/{userId}/password")
    fun updateUserPassword(
        @PathVariable userId: Long,
        @RequestBody(required = true) @Validated userPasswordUpdateDto: UserPasswordUpdateDto
    ): ResponseEntity<UserDto> {
        val updatedPasswordUser = userService.updateUserPassword(userId, userPasswordUpdateDto)
        return ResponseEntity.ok(updatedPasswordUser)
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(
        @PathVariable userId: Long,
        @RequestParam userType: UserType
    ): ResponseEntity<Void> {
        userService.deleteUser(userId, userType)
        return ResponseEntity.noContent().build()
    }
}