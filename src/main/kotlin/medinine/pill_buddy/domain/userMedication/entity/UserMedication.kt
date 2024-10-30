package medinine.pill_buddy.domain.userMedication.entity

import jakarta.persistence.*
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.notification.entity.Notification
import medinine.pill_buddy.domain.record.entity.Record
import medinine.pill_buddy.global.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "user_medication")
class UserMedication(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_medication_id")
    val id: Long? = null,

    @Column(name = "name", length = 30, nullable = false)
    var name: String,

    @Column(name = "description", length = 100)
    var description: String? = null,

    @Column(name = "dosage")
    var dosage: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    var frequency: Frequency,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    var type: MedicationType,

    @Column(name = "stock", nullable = false)
    var stock: Int,

    @Column(name = "expiration_date", nullable = false)
    var expirationDate: LocalDateTime,

    @Column(name = "start_date", nullable = false)
    var startDate: LocalDateTime,

    @Column(name = "end_date", nullable = false)
    var endDate: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caretaker_id")
    var caretaker: Caretaker? = null,

    @OneToMany(mappedBy = "userMedication", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val notificationList: MutableList<Notification> = mutableListOf(),

    @OneToMany(mappedBy = "userMedication", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val records: MutableList<Record> = mutableListOf()
) : BaseTimeEntity() {

    fun updateName(name: String) {
        this.name = name
    }

    fun updateDescription(description: String) {
        this.description = description
    }

    fun updateDosage(dosage: Int?) {
        this.dosage = dosage
    }

    fun updateCaretaker(caretaker: Caretaker) {
        this.caretaker = caretaker
    }
}