package medinine.pill_buddy.domain.user.caretaker.entity

import jakarta.persistence.*
import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.global.entity.BaseTimeEntity

@Entity
@Table(name = "caretaker_caregiver")
class CaretakerCaregiver (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caretaker_caregiver_id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caretaker_id")
    var caretaker: Caretaker? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caregiver_id")
    var caregiver: Caregiver? = null

) : BaseTimeEntity() {

    fun updateUser(caretaker: Caretaker) {
        this.caretaker = caretaker
    }

    fun updateCaregiver(caregiver: Caregiver) {
        this.caregiver = caregiver
    }
}