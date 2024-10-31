package medinine.pill_buddy.domain.notification.controller

import medinine.pill_buddy.domain.notification.dto.NotificationDTO
import medinine.pill_buddy.domain.notification.service.NotificationService
import medinine.pill_buddy.log
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
        log.info("스케줄링 메서드가 실행되었습니다.")
        notificationService.sendNotifications()
    }
}

