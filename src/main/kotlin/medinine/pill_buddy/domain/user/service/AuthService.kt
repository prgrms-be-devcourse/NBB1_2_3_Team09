package medinine.pill_buddy.domain.user.service

import JoinDto
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.user.dto.LoginDto
import medinine.pill_buddy.domain.user.dto.UserDto
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.global.jwt.JwtToken
import medinine.pill_buddy.global.jwt.JwtTokenProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class AuthService(
    private val caretakerRepository: CaretakerRepository,
    private val caregiverRepository: CaregiverRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userDetailService: MyUserDetailService,
    private val jwtTokenProvider: JwtTokenProvider
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

    @Transactional(readOnly = true)
    fun login(loginDto: LoginDto): JwtToken {
        // 로그인 아이디를 통해 회원 조회
        val userDetails = userDetailService.loadUserByUsername(loginDto.loginId)

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(loginDto.password, userDetails.password)) {
            throw PillBuddyCustomException(ErrorCode.USER_MISMATCHED_ID_OR_PASSWORD)
        }

        // 로그인 아이디를 통해 Jwt 토큰 생성 후 반환
        return jwtTokenProvider.generateToken(loginDto.loginId)
    }

    @Transactional(propagation = Propagation.NEVER)
    fun logout() {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw PillBuddyCustomException(ErrorCode.USER_AUTHENTICATION_REQUIRED)

        SecurityContextHolder.clearContext()
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