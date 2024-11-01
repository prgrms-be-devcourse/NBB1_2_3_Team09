package medinine.pill_buddy.domain.userMedication.service

import medinine.pill_buddy.domain.record.dto.RecordDTO
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import java.time.LocalDateTime

interface UserMedicationService {
    fun register(caretakerId: Long, userMedicationDTO: UserMedicationDTO): UserMedicationDTO
    fun retrieve(caretakerId: Long): List<UserMedicationDTO>
    fun modify(caretakerId: Long, userMedicationId: Long, userMedicationDTO: UserMedicationDTO): UserMedicationDTO
    fun getUserMedicationRecordsByDate(caretakerId: Long, date: LocalDateTime): List<RecordDTO>
}