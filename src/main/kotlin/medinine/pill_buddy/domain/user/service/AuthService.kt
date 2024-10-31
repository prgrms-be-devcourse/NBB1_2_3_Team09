package medinine.pill_buddy.domain.user.service

import JoinDto
import jakarta.transaction.Transactional
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.user.dto.UserDto
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Transactional
class AuthService(
    private val caretakerRepository: CaretakerRepository,
    private val caregiverRepository: CaregiverRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun join(joinDto: JoinDto): UserDto {
        validateUserInfo(joinDto.loginId, joinDto.email, joinDto.phoneNumber)

        // 비밀번호 암호화
        val encodedPassword = passwordEncoder.encode(joinDto.password)
        joinDto.changeEncodedPassword(encodedPassword)

        // 사용자 타입에 따라 회원 저장
        return when (joinDto.userType) {
            UserType.CAREGIVER -> UserDto(caregiverRepository.save(joinDto.toCaregiverEntity()))
            UserType.CARETAKER -> UserDto(caretakerRepository.save(joinDto.toCaretakerEntity()))
        }
    }

    private fun validateUserInfo(loginId: String, email: String, phoneNumber: String) {
        require(!caregiverRepository.existsByLoginId(loginId) && !caretakerRepository.existsByLoginId(loginId)) {
            throw PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_LOGIN_ID)
        }

        require(!caregiverRepository.existsByEmail(email) && !caretakerRepository.existsByEmail(email)) {
            throw PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_EMAIL)
        }

        require(!caregiverRepository.existsByPhoneNumber(phoneNumber) && !caretakerRepository.existsByPhoneNumber(phoneNumber)) {
            throw PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_PHONE_NUMBER)
        }
    }
}