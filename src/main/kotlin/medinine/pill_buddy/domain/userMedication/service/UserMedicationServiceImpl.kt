package medinine.pill_buddy.domain.userMedication.service

import medinine.pill_buddy.domain.record.dto.RecordDTO
import org.springframework.transaction.annotation.Transactional
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.log
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

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
    //@Cacheable(cacheNames = ["getRetrieve"], key = "'caretakerId:' + #caretakerId", cacheManager = "redisCacheManager")
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

        userMedicationDTO.name?.let { userMedication.updateName(it) }
        userMedicationDTO.description?.let { userMedication.updateDescription(it) }
        userMedicationDTO.dosage?.let { userMedication.updateDosage(it) }

        return UserMedicationDTO.entityToDTO(userMedication)
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ["getRecordsByDate"], key = "'caretakerId:' + #caretakerId + ':date:' + #date", cacheManager = "redisCacheManager")
    override fun getUserMedicationRecordsByDate(caretakerId: Long, date: LocalDateTime): List<RecordDTO> {
        val userMedications = userMedicationRepository.findByCaretakerId(caretakerId)
        val recordDTOList = mutableListOf<RecordDTO>()

        for (medication in userMedications) {
            val records = medication.records
                .filter { it.date.toLocalDate() == date.toLocalDate() }

            for (record in records) {
                recordDTOList.add(RecordDTO(record))
            }
        }
        return recordDTOList
    }
}