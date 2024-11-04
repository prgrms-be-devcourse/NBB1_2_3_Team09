package medinine.pill_buddy.domain.notification.controller

import medinine.pill_buddy.domain.notification.dto.NotificationDTO
import medinine.pill_buddy.domain.notification.dto.UserNotificationDTO
import medinine.pill_buddy.domain.notification.provider.SmsProvider
import medinine.pill_buddy.domain.notification.repository.NotificationRepository
import medinine.pill_buddy.domain.notification.service.NotificationService
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerCaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var notificationService: NotificationService

    @MockBean
    lateinit var notificationRepository: NotificationRepository

    @MockBean
    lateinit var userMedicationRepository: UserMedicationRepository

    @MockBean
    lateinit var caretakerCaregiverRepository: CaretakerCaregiverRepository

    @MockBean
    lateinit var smsProvider: SmsProvider

    @MockBean
    lateinit var caretakerRepository: CaretakerRepository

    private val BASE_URL = "/api/notification"

    @Test
    @DisplayName("알림 생성")
    fun createNotifications_test() {
        // given
        val userMedicationId = 1L
        val fixedTime = LocalDateTime.of(2024, 11, 1, 9, 0, 0)
        val mockNotificationDTO = NotificationDTO(
            notificationId = 1L,
            medicationName = "Medication A",
            frequency = Frequency.ONCE_A_DAY,
            notificationTime = fixedTime,
            caretakerId = 1L
        )

        `when`(notificationService.createNotifications(userMedicationId))
            .thenReturn(listOf(mockNotificationDTO))

        // when & then
        mvc.perform(
            post("$BASE_URL/$userMedicationId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                content().json(
                    """
            [
                {
                    "notificationId": 1,
                    "medicationName": "Medication A",
                    "frequency": "ONCE_A_DAY",
                    "notificationTime": "2024-11-01T09:00:00",
                    "caretakerId": 1
                }
            ]
            """
                )
            )
    }

    @Test
    @DisplayName("알림 조회")
    fun findNotifications_test() {
        // given
        val fixedTime = LocalDateTime.of(2024, 11, 1, 9, 0, 0)
        val caretakerId = 1L
        val userNotificationDTO = UserNotificationDTO(
            notificationId = 1L,
            notificationTime = fixedTime,
            caretakerId = caretakerId,
            caretakerUsername = "사용자",
            medicationName = "Medication A",
            frequency = Frequency.ONCE_A_DAY
        )

        `when`(notificationService.findNotification(caretakerId))
            .thenReturn(listOf(userNotificationDTO))

        // when & then
        mvc.perform(
            get("$BASE_URL/$caretakerId")
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json("""
            [
                {
                    "notificationId": 1,
                    "notificationTime": "2024-11-01T09:00:00",
                    "caretakerId": 1,
                    "caretakerUsername": "사용자",
                    "medicationName": "Medication A",
                    "frequency": "ONCE_A_DAY"
                }
            ]
            """))
    }
}
