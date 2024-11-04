package medinine.pill_buddy.domain.notification.controller

import medinine.pill_buddy.domain.notification.dto.NotificationDTO
import medinine.pill_buddy.domain.notification.dto.UserNotificationDTO
import medinine.pill_buddy.domain.notification.service.NotificationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/notification")
class NotificationController(
    private val notificationService: NotificationService
) {

    @PostMapping("/{userMedicationId}")
    fun createNotifications(@PathVariable userMedicationId: Long): ResponseEntity<List<NotificationDTO>> {
        val notifications = notificationService.createNotifications(userMedicationId)
        return ResponseEntity.status(HttpStatus.CREATED).body(notifications)
    }

    @Scheduled(cron = "0 * * * * ?")
    fun checkAndSendNotifications() {
        notificationService.sendNotifications(LocalDateTime.now())
        notificationService.checkAndSendForMissedMedications(LocalDateTime.now())
    }

    @GetMapping("/{caretakerId}")
    fun findNotifications(@PathVariable caretakerId: Long): ResponseEntity<List<UserNotificationDTO>> {
        return ResponseEntity.ok(notificationService.findNotification(caretakerId))
    }
}

