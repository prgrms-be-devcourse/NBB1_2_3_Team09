package medinine.pill_buddy.domain.notification.dto

import medinine.pill_buddy.domain.notification.entity.Notification
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import java.time.LocalDateTime

data class NotificationDTO(
    val notificationId: Long? = null,
    val medicationName: String? = null,
    val frequency: Frequency? = null,
    val notificationTime: LocalDateTime? = null,
    val caretakerId: Long? = null
) {
    constructor(notification: Notification) : this(
        notificationId = notification.id,
        medicationName = notification.userMedication?.name,
        frequency = notification.userMedication?.frequency,
        notificationTime = notification.notificationTime,
        caretakerId = notification.caretaker?.id
    )

    companion object {
        fun convertToDTOs(notifications: List<Notification>): List<NotificationDTO> {
            return notifications.map { NotificationDTO(it) }
        }

        fun convertToDTO(notification: Notification): NotificationDTO {
            return NotificationDTO(notification)
        }
    }
}
