package medinine.pill_buddy.domain.notification.dto

import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

data class UpdateNotificationDTO(
    @field:NotNull
    val notificationTime: LocalDateTime
)