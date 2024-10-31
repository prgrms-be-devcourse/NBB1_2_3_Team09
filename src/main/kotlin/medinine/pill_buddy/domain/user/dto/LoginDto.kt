package medinine.pill_buddy.domain.user.dto

import jakarta.validation.constraints.NotBlank

data class LoginDto(
    @field:NotBlank
    val loginId : String,

    @field:NotBlank
    val password : String
)