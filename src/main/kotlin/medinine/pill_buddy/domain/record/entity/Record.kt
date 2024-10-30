package medinine.pill_buddy.domain.record.entity

import jakarta.persistence.*
import medinine.pill_buddy.domain.userMedication.entity.UserMedication
import medinine.pill_buddy.global.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "record")
class Record(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    var id: Long? = null,

    @Column(name = "date", nullable = false)
    var date: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "taken", nullable = false)
    var taken: Taken,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_medication_id")
    var userMedication: UserMedication? = null
) : BaseTimeEntity() {

    fun changeUserMedication(userMedication: UserMedication) {
        this.userMedication = userMedication
        userMedication.records.add(this)
    }

    fun takeMedication() {
        this.taken = Taken.TAKEN
    }
}