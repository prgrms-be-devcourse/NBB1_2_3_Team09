package medinine.pill_buddy.domain.medicationApi.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
class Medication(

    @Id
    var itemSeq: Long,

    @Column(length = 100)
    var entpName: String,

    @Column(nullable = false, length = 100)
    var itemName: String,

    @Column(columnDefinition = "text")
    var efcyQesitm: String,

    @Column(columnDefinition = "text")
    var useMethodQesitm: String,

    @Column(columnDefinition = "text")
    var atpnWarnQesitm: String,

    @Column(columnDefinition = "text")
    var atpnQesitm: String,

    @Column(columnDefinition = "text")
    var intrcQesitm: String,

    @Column(columnDefinition = "text")
    var seQesitm: String,

    @Column(columnDefinition = "text")
    var depositMethodQesitm: String,

    @Column(columnDefinition = "text")
    var itemImagePath: String
) : Persistable<Long> {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private var createdAt: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "modified_at")
    private var modifiedAt: LocalDateTime? = null

    override fun getId(): Long = itemSeq

    override fun isNew(): Boolean = createdAt == null
}