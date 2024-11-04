package medinine.pill_buddy.domain.record.service

import medinine.pill_buddy.domain.record.dto.RecordDTO

interface RecordService {
    fun modifyTaken(userMedicationId: Long, recordId: Long): RecordDTO
    fun registerRecord(userMedicationId: Long): RecordDTO
}