package medinine.pill_buddy.domain.record.dto

import com.fasterxml.jackson.annotation.JsonFormat
import medinine.pill_buddy.domain.record.entity.Record
import medinine.pill_buddy.domain.record.entity.Taken
import java.time.LocalDateTime

class RecordDTO (
    val id: Long?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    val date: LocalDateTime,
    val medicationName: String?,
    val taken: Taken
) {
    constructor(record: Record): this (
        id = record.id,
        date = record.date,
        medicationName = record.userMedication?.name,
        taken = record.taken
    )
}