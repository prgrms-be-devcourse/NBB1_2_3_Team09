package medinine.pill_buddy.domain.user.profile.controller

import medinine.pill_buddy.domain.user.profile.dto.ProfileUploadDto
import medinine.pill_buddy.domain.user.profile.service.ProfileService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/users/profile")
class ProfileController(
    private val profileService: ProfileService
) {
    @PostMapping("/{userId}")
    fun upload(
        @PathVariable userId: Long,
        @ModelAttribute @Validated profileUploadDto: ProfileUploadDto
    ): ResponseEntity<Unit> {
        profileService.uploadProfile(userId, profileUploadDto)
        return ResponseEntity.noContent().build()
    }
}