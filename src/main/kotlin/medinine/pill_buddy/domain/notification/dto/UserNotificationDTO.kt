package medinine.pill_buddy.domain.notification.dto

import medinine.pill_buddy.domain.notification.entity.Notification
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import java.time.LocalDateTime

data class UserNotificationDTO(
    val notificationId: Long? = null,
    val notificationTime: LocalDateTime? = null,
    val caretakerId: Long? = null,
    val caretakerUsername: String? = null,
    val medicationName: String? = null,
    val frequency: Frequency? = null,
) {
    constructor(notification: Notification) : this(
        notificationId = notification.id,
        notificationTime = notification.notificationTime,
        caretakerId = notification.caretaker?.id,
        caretakerUsername = notification.caretaker?.username,
        medicationName = notification.userMedication?.name,
        frequency = notification.userMedication?.frequency,
    )
}