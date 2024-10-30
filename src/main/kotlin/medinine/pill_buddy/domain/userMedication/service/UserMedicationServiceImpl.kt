package medinine.pill_buddy.domain.userMedication.service

import jakarta.transaction.Transactional
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.log
import org.springframework.stereotype.Service

@Service
class UserMedicationServiceImpl(
    private val userMedicationRepository: UserMedicationRepository,
    private val caretakerRepository: CaretakerRepository
) : UserMedicationService {

    @Transactional
    override fun register(caretakerId: Long, userMedicationDTO: UserMedicationDTO): UserMedicationDTO {
        val caretaker = caretakerRepository.findById(caretakerId)
            .orElseThrow { PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND) }

        val userMedication = userMedicationDTO.toEntity()
        userMedication.updateCaretaker(caretaker)
        val savedUserMedication = userMedicationRepository.save(userMedication)

        log.info("Saved userMedication: {}", savedUserMedication)

        return UserMedicationDTO.entityToDTO(savedUserMedication)
    }
}