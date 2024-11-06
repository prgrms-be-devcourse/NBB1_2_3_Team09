package medinine.pill_buddy.domain.record.service

import medinine.pill_buddy.domain.record.dto.RecordDTO
import medinine.pill_buddy.domain.record.entity.Record
import medinine.pill_buddy.domain.record.entity.Taken
import medinine.pill_buddy.domain.record.repository.RecordRepository
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class RecordServiceImpl (
    private val userMedicationRepository: UserMedicationRepository,
    private val recordRepository: RecordRepository
) : RecordService {

    @Transactional
    override fun registerRecord(userMedicationId: Long): RecordDTO {
        val userMedication = userMedicationRepository.findById(userMedicationId)
            .orElseThrow { PillBuddyCustomException(ErrorCode.MEDICATION_NOT_FOUND) }

        val record = Record(
            date = LocalDateTime.now(),
            taken = Taken.UNTAKEN,
            userMedication = userMedication
        )

        val savedRecord = recordRepository.save(record)
        return RecordDTO(savedRecord)
    }

    @Transactional
    override fun modifyTaken(userMedicationId: Long, recordId: Long): RecordDTO {
        val userMedication = userMedicationRepository.findById(userMedicationId)
            .orElseThrow { PillBuddyCustomException(ErrorCode.MEDICATION_NOT_FOUND) }

        val record = userMedication.records
            .firstOrNull { it.id == recordId }
            ?: throw PillBuddyCustomException(ErrorCode.RECORD_NOT_FOUND)

        if (record.taken == Taken.UNTAKEN) {
            record.takeMedication()
        } else if (record.taken == Taken.TAKEN) {
            throw PillBuddyCustomException(ErrorCode.RECORD_ALREADY_TAKEN)
        }

        return RecordDTO(record)
    }
}