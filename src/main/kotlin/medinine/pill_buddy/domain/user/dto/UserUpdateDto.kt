package medinine.pill_buddy.domain.user.dto

import jakarta.validation.constraints.*

class UserUpdateDto(
    @Size(min = 2, max = 30)
    val username: String?,

    @Size(min = 5, max = 20)
    val loginId: String?,

    @field:Email
    @field:Size(max = 50)
    val email: String?,

    @field:Size(min = 10, max = 12)
    @field:Pattern(regexp = "^[0-9]+$")
    val phoneNumber: String?,

    @field:NotNull
    val userType: UserType
)