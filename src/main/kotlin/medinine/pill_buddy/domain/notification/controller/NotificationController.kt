package medinine.pill_buddy.domain.notification.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import medinine.pill_buddy.domain.notification.dto.NotificationDTO
import medinine.pill_buddy.domain.notification.dto.UpdateNotificationDTO
import medinine.pill_buddy.domain.notification.service.NotificationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/notification")
@Tag(name = "알림 기능", description = "사용자는 알림 기능을 등록,조회,삭제,수정할 수 있다.")
class NotificationController(
    private val notificationService: NotificationService
) {

    @Operation(description = "사용자는 등록한 약 정보를 통해 알림을 생성할 수 있다.")
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

    @Operation(description = "사용자는 설정된 알림 정보를 조회할 수 있다.")
    @GetMapping("/{caretakerId}")
    fun findNotifications(@PathVariable caretakerId: Long): ResponseEntity<List<NotificationDTO>> {
        return ResponseEntity.ok(notificationService.findNotification(caretakerId))
    }

    @Operation(description = "사용자는 설정된 알림 시간을 수정할 수 있다.")
    @PatchMapping("/{notificationId}/time")
    fun updateNotification(@PathVariable notificationId: Long,
                           @RequestBody @Valid updateNotification: UpdateNotificationDTO): ResponseEntity<NotificationDTO> {
        return ResponseEntity.ok(notificationService.updateNotification(notificationId, updateNotification))
    }

    @Operation(description = "사용자는 설정된 알림 정보를 삭제할 수 있다.")
    @DeleteMapping("/{notificationId}")
    fun deleteNotification(@PathVariable notificationId: Long): ResponseEntity<Void> {
        notificationService.deleteNotification(notificationId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}

