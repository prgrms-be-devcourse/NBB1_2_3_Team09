package medinine.pill_buddy.domain.userMedication.service

import org.springframework.transaction.annotation.Transactional
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

    @Transactional(readOnly = true)
    override fun retrieve(caretakerId: Long): List<UserMedicationDTO> {
        val medications = userMedicationRepository.findByCaretakerId(caretakerId)
        log.info("Retrieved medications: {}", medications)

        return medications.map { UserMedicationDTO.entityToDTO(it) }
    }

    @Transactional
    override fun modify(
        caretakerId: Long,
        userMedicationId: Long,
        userMedicationDTO: UserMedicationDTO
    ): UserMedicationDTO {
        val userMedication = userMedicationRepository.findById(userMedicationId).orElseThrow {
            PillBuddyCustomException(ErrorCode.MEDICATION_NOT_FOUND)
        }

        if (userMedication.caretaker?.id != caretakerId) {
            throw PillBuddyCustomException(ErrorCode.MEDICATION_NOT_VALID)
        }
        return try {
            userMedicationDTO.name?.let { userMedication.updateName(it) }
            userMedicationDTO.description?.let { userMedication.updateDescription(it) }
            userMedicationDTO.dosage?.let { userMedication.updateDosage(it) }

            UserMedicationDTO.entityToDTO(userMedication)
        } catch (e: PillBuddyCustomException) {
            log.error(e.message, e)
            throw e
        }
    }
}