package medinine.pill_buddy.domain.notification.repository

import medinine.pill_buddy.domain.notification.entity.Notification
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
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
    private var notificationRepository: NotificationRepository,

    @Autowired
    private var caretakerRepository: CaretakerRepository
) {
    private val fixedTime: LocalDateTime = LocalDateTime.of(2024, 11, 1, 9, 0, 0)

    @BeforeEach
    fun setUp() {
        val caretaker = Caretaker(
            username = "CtUsername",
            loginId = "CtLoginId",
            password = "CtPassword",
            email = "CtEmail",
            phoneNumber = "CtPhoneNumber"
        )
        caretakerRepository.save(caretaker)

        val notification = Notification(
            notificationTime = fixedTime,
            userMedication = null,
            caretaker = caretaker
        )
        notificationRepository.save(notification)
    }

    @Test
    @DisplayName("주어진 시간으로 등록된 알림 조회")
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

    @Test
    @DisplayName("주어진 사용자가 보유한 알림 조회")
    fun findByCaretaker() {
        //given
        val caretaker = caretakerRepository.findAll().first()

        //when
        val userNotificationDTOS = notificationRepository.findByCaretaker(caretaker)

        //then
        assertThat(userNotificationDTOS).hasSize(1)
        assertThat(userNotificationDTOS[0].notificationTime).isEqualTo(fixedTime)
    }
}