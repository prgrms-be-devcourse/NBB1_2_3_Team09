package medinine.pill_buddy.domain.user.profile.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import medinine.pill_buddy.domain.user.profile.dto.ProfileUploadDto
import medinine.pill_buddy.domain.user.profile.service.ProfileService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/users/profile")
@Tag(name = "사진 등록 기능",description = "사용자는 프로필 사진을 등록할 수 있다.")
class ProfileController(
    private val profileService: ProfileService
) {
    @Operation(description = "사용자는 프로필 사진을 등록할 수 있다.")
    @PostMapping("/{userId}")
    fun upload(
        @PathVariable userId: Long,
        @ModelAttribute @Validated profileUploadDto: ProfileUploadDto
    ): ResponseEntity<Unit> {
        profileService.uploadProfile(userId, profileUploadDto)
        return ResponseEntity.noContent().build()
    }
}