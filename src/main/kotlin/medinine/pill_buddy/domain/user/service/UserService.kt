package medinine.pill_buddy.domain.user.service

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.user.dto.UserDto
import medinine.pill_buddy.domain.user.dto.UserPasswordUpdateDto
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.dto.UserUpdateDto
import medinine.pill_buddy.domain.user.entity.User
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val caretakerRepository: CaretakerRepository,
    private val caregiverRepository: CaregiverRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ["getUser"], key = "'userId:' + #userId + ':userType:' + #userType", cacheManager = "redisCacheManager")
    fun findUser(userId: Long, userType: UserType): UserDto {
        return UserDto(findUserByIdAndUserType(userId, userType))
    }

    fun updateUserInfo(userId: Long, userUpdateDto: UserUpdateDto): UserDto {
        val user = findUserByIdAndUserType(userId, userUpdateDto.userType)

        validateUserInfo(userUpdateDto.loginId, userUpdateDto.email, userUpdateDto.phoneNumber)

        user.apply {
            updateUsername(userUpdateDto.username ?: user.username)
            updateLoginId(userUpdateDto.loginId ?: user.loginId)
            updateEmail(userUpdateDto.email ?: user.email)
            (userUpdateDto.phoneNumber ?: user.phoneNumber)?.let { updatePhoneNumber(it) }
        }
        return UserDto(user)
    }

    fun updateUserPassword(userId: Long, userPasswordUpdateDto: UserPasswordUpdateDto): UserDto {
        val user = findUserByIdAndUserType(userId, userPasswordUpdateDto.userType)
        val encodedPassword = passwordEncoder.encode(userPasswordUpdateDto.password)

        user.updatePassword(encodedPassword)

        return UserDto(user)
    }

    fun deleteUser(userId: Long, userType: UserType) {
        when (val user = findUserByIdAndUserType(userId, userType)) {
            is Caretaker -> caretakerRepository.delete(user)
            is Caregiver -> caregiverRepository.delete(user)
        }
        // TODO 2024/10/31 : Image 삭제 추가
    }

    private fun validateUserInfo(loginId: String?, email: String?, phoneNumber: String?) {
        loginId?.let {
            require(!caregiverRepository.existsByLoginId(loginId) && !caretakerRepository.existsByLoginId(loginId)) {
                throw PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_LOGIN_ID)
            }
        }
        email?.let {
            require(!caregiverRepository.existsByEmail(email) && !caretakerRepository.existsByEmail(email)) {
                throw PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_EMAIL)
            }
        }
        phoneNumber?.let {
            require(!caregiverRepository.existsByPhoneNumber(phoneNumber) && !caretakerRepository.existsByPhoneNumber(phoneNumber)) {
                throw PillBuddyCustomException(ErrorCode.USER_ALREADY_REGISTERED_PHONE_NUMBER)
            }
        }
    }

    private fun findUserByIdAndUserType(userId: Long, userType: UserType): User {
        return when (userType) {
            UserType.CAREGIVER -> caregiverRepository.findById(userId)
                .orElseThrow { PillBuddyCustomException(ErrorCode.USER_NOT_FOUND) }

            UserType.CARETAKER -> caretakerRepository.findById(userId)
                .orElseThrow { PillBuddyCustomException(ErrorCode.USER_NOT_FOUND) }
        }
    }
}