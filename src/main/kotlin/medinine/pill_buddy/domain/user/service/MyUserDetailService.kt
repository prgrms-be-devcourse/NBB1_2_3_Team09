package medinine.pill_buddy.domain.user.service

import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MyUserDetailService(
    private val caretakerRepository: CaretakerRepository,
    private val caregiverRepository: CaregiverRepository
) : UserDetailsService {
    override fun loadUserByUsername(loginId: String): CustomUserDetails {
        return caretakerRepository.findByLoginId(loginId)?.let {
            CustomUserDetails(it)
        } ?: caregiverRepository.findByLoginId(loginId)?.let {
            CustomUserDetails(it)
        } ?: throw PillBuddyCustomException(ErrorCode.USER_NOT_FOUND)
    }
}