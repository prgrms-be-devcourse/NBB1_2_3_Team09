package medinine.pill_buddy.global.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeEntity (

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private val createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "modified_at")
    private val modifiedAt: LocalDateTime? = null
)