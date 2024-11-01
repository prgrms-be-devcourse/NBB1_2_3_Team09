package medinine.pill_buddy.domain.notification.repository

import medinine.pill_buddy.domain.notification.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime


interface NotificationRepository : JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.notificationTime >= :now AND n.notificationTime < :nowPlusOneMinute")
    fun findByNotificationTime(@Param("now") now: LocalDateTime, @Param("nowPlusOneMinute") nowPlusOneMinute: LocalDateTime): List<Notification>
}