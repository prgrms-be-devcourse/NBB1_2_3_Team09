package medinine.pill_buddy.domain.notification.controller

import medinine.pill_buddy.domain.notification.dto.UpdateNotificationDTO
import medinine.pill_buddy.domain.notification.service.NotificationService
import medinine.pill_buddy.domain.userMedication.service.UserMedicationServiceImpl
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Controller
@RequestMapping("/notifications")
class NotificationWebController(
    private val notificationService: NotificationService,
    private val userMedicationServiceImpl: UserMedicationServiceImpl
) {

    // 알림 목록 페이지
    @GetMapping("/{caretakerId}")
    fun showNotifications(@PathVariable("caretakerId") caretakerId: Long, model: Model): String {
        val notifications = notificationService.findNotification(caretakerId)
        model.addAttribute("notifications", notifications)
        return "notifications" // notifications.html 뷰를 반환
    }

    // 알림 목록 페이지 -> 알림 추가 버튼
    @GetMapping("/create/{caretakerId}")
    fun createNotificationForm(@PathVariable("caretakerId") caretakerId: Long, model: Model): String {
        val userMedications = userMedicationServiceImpl.retrieve(caretakerId)
        model.addAttribute("caretakerId", caretakerId)
        model.addAttribute("userMedications", userMedications)
        return "create_notification" // createNotification.html 뷰를 반환
    }

    // 알림 추가 화면 - >추가 버튼
    @PostMapping("/create")
    fun createNotification(@RequestParam userMedicationId: Long,
                           @RequestParam caretakerId: Long): String {
        notificationService.createNotifications(userMedicationId)
        return "redirect:/notifications/$caretakerId" // 생성 후 해당 사용자의 알림 페이지로 리다이렉트
    }

    // 알림 목록 페이지 -> 알림 수정 버튼
    @GetMapping("/update/{notificationId}/{caretakerId}")
    fun updateNotificationForm(@PathVariable("notificationId") notificationId: Long,
                               @PathVariable("caretakerId") caretakerId: Long,
                               model: Model): String {
        model.addAttribute("notificationId", notificationId)
        model.addAttribute("caretakerId", caretakerId)
        return "update_notification" // update_notification.html 뷰를 반환
    }

    // 알림 수정 화면 -> 수정하기 버튼
    @PostMapping("/update/{notificationId}")
    fun updateNotification(@PathVariable("notificationId") notificationId: Long,
                           @RequestParam notificationTime: LocalDateTime
    ): String {
        val updateNotification = UpdateNotificationDTO(notificationTime)
        val updatedNotification = notificationService.updateNotification(notificationId, updateNotification)
        val caretakerId = updatedNotification.caretakerId
        return "redirect:/notifications/${caretakerId}" // 수정 후 해당 사용자의 알림 페이지로 리다이렉트
    }

    // 알림 삭제
    @PostMapping("/delete/{notificationId}/{caretakerId}")
    fun deleteNotification(@PathVariable notificationId: Long, @PathVariable("caretakerId") caretakerId: Long): String {
        notificationService.deleteNotification(notificationId)
        return "redirect:/notifications/${caretakerId}" // 삭제 후 알림 목록 페이지로 리다이렉트
    }
}

