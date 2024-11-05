package medinine.pill_buddy.domain.user.oauth.service

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.dto.UserType.CAREGIVER
import medinine.pill_buddy.domain.user.dto.UserType.CARETAKER
import medinine.pill_buddy.domain.user.oauth.dto.OAuthProfile
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserReader(
    @Value("\${oauth.oauth2-password}")
    private val oauth2Password: String,
    private val caretakerRepository: CaretakerRepository,
    private val caregiverRepository: CaregiverRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun isNewUser(email: String, userType: UserType): Boolean {
        return when (userType) {
            CAREGIVER -> caregiverRepository.existsByEmail(email)
            CARETAKER -> caretakerRepository.existsByEmail(email)
        }
    }

    fun registerUser(profile: OAuthProfile, userType: UserType, registrationId: String) {
        val loginId = "${registrationId}_${profile.id}"
        val encodedPassword = passwordEncoder.encode(oauth2Password)
        val username = profile.nickname
        val phoneNumber = profile.phoneNumber?.replace("-", "")

        when (userType) {
            CARETAKER -> {
                val caretaker = Caretaker(
                    username = username,
                    loginId = loginId,
                    password = encodedPassword,
                    email = profile.email,
                    phoneNumber = phoneNumber
                )
                caretakerRepository.save(caretaker)
            }

            CAREGIVER -> {
                val caregiver = Caregiver(
                    username = username,
                    loginId = loginId,
                    password = encodedPassword,
                    email = profile.email,
                    phoneNumber = phoneNumber
                )
                caregiverRepository.save(caregiver)
            }
        }
    }

    fun getUserLoginID(email: String, userType: UserType): String {
        return when (userType) {
            CAREGIVER -> caregiverRepository.findByEmail(email)?.loginId
                ?: throw PillBuddyCustomException(ErrorCode.USER_NOT_FOUND)

            CARETAKER -> caretakerRepository.findByEmail(email)?.loginId
                ?: throw PillBuddyCustomException(ErrorCode.USER_NOT_FOUND)
        }
    }
}