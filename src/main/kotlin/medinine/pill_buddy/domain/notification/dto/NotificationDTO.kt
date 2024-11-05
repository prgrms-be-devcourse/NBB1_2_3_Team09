package medinine.pill_buddy.domain.notification.dto

import medinine.pill_buddy.domain.notification.entity.Notification
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import java.time.LocalDateTime

data class NotificationDTO(
    val notificationId: Long? = null,
    val notificationTime: LocalDateTime? = null,
    val caretakerId: Long? = null,
    val caretakerUsername: String? = null,
    val medicationId: Long? = null,
    val medicationName: String? = null,
    val frequency: Frequency? = null,
) {
    constructor(notification: Notification) : this(
        notificationId = notification.id,
        notificationTime = notification.notificationTime,
        caretakerId = notification.caretaker?.id,
        caretakerUsername = notification.caretaker?.username,
        medicationId = notification.userMedication?.id,
        medicationName = notification.userMedication?.name,
        frequency = notification.userMedication?.frequency,
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
