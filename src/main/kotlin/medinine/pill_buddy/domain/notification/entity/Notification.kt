package medinine.pill_buddy.domain.notification.entity

import jakarta.persistence.*
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.userMedication.entity.UserMedication
import medinine.pill_buddy.global.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "notification")
class Notification(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    var id: Long? = null,

    @Column(name = "notification_time", nullable = false)
    var notificationTime: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_medication_id")
    var userMedication: UserMedication? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caretaker_id")
    var caretaker: Caretaker? = null
) : BaseTimeEntity() {
    fun changeUserMedication(userMedication: UserMedication) {
        this.userMedication = userMedication
        userMedication.notificationList.add(this)
    }

    fun changeNotificationTime(notificationTime: LocalDateTime) {
        this.notificationTime = notificationTime
    }
}