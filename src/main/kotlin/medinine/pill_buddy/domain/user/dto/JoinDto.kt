import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.dto.UserType

data class JoinDto(

    @field:NotBlank
    @field:Size(min = 2, max = 30)
    val username: String,

    @field:NotBlank
    @field:Size(min = 5, max = 20)
    val loginId: String,

    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
        message = "비밀번호는 최소 8자 이상이어야 하고, 적어도 하나의 영문자, 숫자, 특수문자를 포함해야 합니다."
    )
    @field:Size(min = 8, max = 30)
    var password: String,

    @field:NotBlank
    @field:Email
    @field:Size(max = 50)
    val email: String,

    @field:NotBlank
    @field:Size(min = 10, max = 12)
    @field:Pattern(regexp = "^[0-9]+$")
    val phoneNumber: String,

    @field:NotNull
    val userType: UserType
) {

    fun changeEncodedPassword(encodedPassword: String) {
        this.password = encodedPassword
    }

    fun toCaregiverEntity(): Caregiver {
        return Caregiver(
            username = username,
            loginId = loginId,
            password = password,
            email = email,
            phoneNumber = phoneNumber
        )
    }

    fun toCaretakerEntity(): Caretaker {
        return Caretaker(
            username = username,
            loginId = loginId,
            password = password,
            email = email,
            phoneNumber = phoneNumber
        )
    }
}