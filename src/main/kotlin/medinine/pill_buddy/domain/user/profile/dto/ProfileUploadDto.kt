package medinine.pill_buddy.domain.user.profile.dto

import jakarta.validation.constraints.NotNull
import medinine.pill_buddy.domain.user.dto.UserType
import org.springframework.web.multipart.MultipartFile

data class ProfileUploadDto(

    @field:NotNull
    val file : MultipartFile,

    @field:NotNull
    val userType: UserType
)