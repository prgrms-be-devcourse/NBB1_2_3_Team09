package medinine.pill_buddy.domain.record.repository

import medinine.pill_buddy.domain.record.entity.Record
import org.springframework.data.jpa.repository.JpaRepository

interface RecordRepository: JpaRepository<Record, Long> {
}