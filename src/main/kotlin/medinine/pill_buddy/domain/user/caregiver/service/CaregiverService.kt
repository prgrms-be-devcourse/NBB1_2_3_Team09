package medinine.pill_buddy.domain.user.caregiver.service

import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.entity.CaretakerCaregiver
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CaregiverService(
    private val caretakerRepository: CaretakerRepository,
    private val caregiverRepository: CaregiverRepository,
    private val caretakerCaregiverRepository: CaretakerCaregiverRepository,
    private val userMedicationRepository: UserMedicationRepository
) {
    @Transactional(readOnly = true)
    fun getCaretakerMedications(caregiverId: Long, caretakerId: Long): List<UserMedicationDTO> {
        caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretakerId, caregiverId)
            ?: throw PillBuddyCustomException(ErrorCode.CAREGIVER_CARETAKER_NOT_MATCHED)

        val medications = userMedicationRepository.findByCaretakerId(caretakerId)
            .map { UserMedicationDTO.entityToDTO(it) }

        if (medications.isEmpty()) {
            throw PillBuddyCustomException(ErrorCode.MEDICATION_IS_NULL)
        }

        return medications
    }

    fun register(caregiverId: Long, caretakerId: Long): CaretakerCaregiverDTO {
        val caregiver = caregiverRepository.findByIdWithImage(caregiverId)
            ?: throw PillBuddyCustomException(ErrorCode.CAREGIVER_NOT_FOUND)
        val caretaker = caretakerRepository.findByIdWithImage(caretakerId)
            ?: throw PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND)

        if (caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretakerId, caregiverId) != null) {
            throw PillBuddyCustomException(ErrorCode.CARETAKER_CAREGIVER_NOT_REGISTERED)
        }

        val caretakerCaregiver = caretakerCaregiverRepository.save(
            CaretakerCaregiver(caregiver = caregiver, caretaker = caretaker)
        )
        return CaretakerCaregiverDTO.entityToDTO(caretakerCaregiver)
    }

    fun remove(caregiverId: Long, caretakerId: Long) {
        val caretakerCaregiver = caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretakerId, caregiverId)
            ?: throw PillBuddyCustomException(ErrorCode.CAREGIVER_CARETAKER_NOT_MATCHED)

        caretakerCaregiverRepository.delete(caretakerCaregiver)
    }
}