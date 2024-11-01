package medinine.pill_buddy.domain.notification.repository

import medinine.pill_buddy.domain.notification.entity.Notification
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDateTime

@DataJpaTest
class NotificationRepositoryTest(
    @Autowired
    private var notificationRepository: NotificationRepository
) {
    private val fixedTime: LocalDateTime = LocalDateTime.of(2024, 11, 1, 9, 0, 0)

    @BeforeEach
    fun setUp() {
        val notification = Notification(
            notificationTime = fixedTime,
            userMedication = null,
            caretaker = null
        )
        notificationRepository.save(notification)
    }

    @Test
    @DisplayName("현재 시간에 존재하는 알림을 찾을 수 있어야 한다.")
    fun findByNotificationTime() {
        // given
        val now = fixedTime
        val nowPlusOneMinute = fixedTime.plusMinutes(1)

        // when
        val notifications = notificationRepository.findByNotificationTime(now, nowPlusOneMinute)

        // then
        assertThat(notifications).hasSize(1)
        assertThat(notifications[0].notificationTime).isEqualTo(fixedTime)
    }
}