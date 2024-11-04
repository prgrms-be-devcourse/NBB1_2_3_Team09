package medinine.pill_buddy.domain.user.caregiver.service

import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import medinine.pill_buddy.domain.user.caretaker.entity.CaretakerCaregiver
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerCaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
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

    @Transactional
    fun register(caregiverId: Long, caretakerId: Long): CaretakerCaregiverDTO {
        val caregiver = caregiverRepository.findById(caregiverId)
            .orElseThrow { PillBuddyCustomException(ErrorCode.CAREGIVER_NOT_FOUND) }
        val caretaker = caretakerRepository.findById(caretakerId)
            .orElseThrow { PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND) }

        caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretakerId, caregiverId)?.let {
            throw PillBuddyCustomException(ErrorCode.CARETAKER_ALREADY_REGISTERED)
        }

        val caretakerCaregiver = CaretakerCaregiver(caregiver = caregiver, caretaker = caretaker)
        val savedCaretakerCaregiver = caretakerCaregiverRepository.save(caretakerCaregiver)
        return CaretakerCaregiverDTO.entityToDTO(savedCaretakerCaregiver)
    }

    @Transactional
    fun remove(caregiverId: Long, caretakerId: Long) {
        val caretakerCaregiver = caretakerCaregiverRepository.findByCaretakerIdAndCaregiverId(caretakerId, caregiverId)
            ?: throw PillBuddyCustomException(ErrorCode.CAREGIVER_CARETAKER_NOT_MATCHED)

        caretakerCaregiverRepository.delete(caretakerCaregiver)
    }
}