package medinine.pill_buddy.domain.user.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class UserPasswordUpdateDto(
    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
        message = "비밀번호는 최소 8자 이상이어야 하고, 적어도 하나의 영문자, 숫자, 특수문자를 포함해야 합니다."
    )
    @field:Size(min = 8, max = 30)
    val password: String,

    @field:NotNull
    val userType: UserType
)