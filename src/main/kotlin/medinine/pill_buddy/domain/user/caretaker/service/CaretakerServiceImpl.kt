package medinine.pill_buddy.domain.user.caretaker.service

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.entity.CaretakerCaregiver
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerCaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CaretakerServiceImpl (
    private val caretakerCaregiverRepository: CaretakerCaregiverRepository,
    private val caretakerRepository: CaretakerRepository,
    private val caregiverRepository: CaregiverRepository
) : CaretakerService {

    @Transactional
    override fun register(caretakerId: Long, caregiverId: Long): CaretakerCaregiverDTO {
        val caretaker: Caretaker = caretakerRepository.findById(caretakerId).orElse(null)
            ?: throw PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND)

        val caregiver: Caregiver = caregiverRepository.findById(caregiverId).orElse(null)
            ?: throw PillBuddyCustomException(ErrorCode.CAREGIVER_NOT_FOUND)

        if (caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretakerId, caregiverId) != null) {
            throw PillBuddyCustomException(ErrorCode.CARETAKER_CAREGIVER_NOT_REGISTERED)
        }

        val caretakerCaregiver = CaretakerCaregiver(
            caretaker = caretaker,
            caregiver = caregiver
        )

        val savedCaretakerCaregiver = caretakerCaregiverRepository.save(caretakerCaregiver)
        return CaretakerCaregiverDTO.entityToDTO(savedCaretakerCaregiver)
    }
}