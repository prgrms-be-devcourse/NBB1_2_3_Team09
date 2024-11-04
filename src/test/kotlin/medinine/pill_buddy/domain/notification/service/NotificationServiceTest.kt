package medinine.pill_buddy.domain.notification.service

import medinine.pill_buddy.domain.notification.entity.Notification
import medinine.pill_buddy.domain.notification.provider.SmsProvider
import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.entity.CaretakerCaregiver
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerCaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.entity.MedicationType
import medinine.pill_buddy.domain.userMedication.entity.UserMedication
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime

@SpringBootTest
class NotificationServiceTest(
    @Autowired
    private val notificationService: NotificationService,

    @Autowired
    private val userMedicationRepository: UserMedicationRepository,

    @Autowired
    private val caretakerRepository: CaretakerRepository,

    @Autowired
    private val caretakerCaregiverRepository: CaretakerCaregiverRepository,

    @Autowired
    private val caregiverRepository: CaregiverRepository
) {
    @MockBean
    private lateinit var smsProvider: SmsProvider

    private val fixedTime: LocalDateTime = LocalDateTime.of(2024, 11, 1, 9, 0, 0)

    @BeforeEach
    fun setUp() {
        caretakerCaregiverRepository.deleteAll()
        caregiverRepository.deleteAll()
        userMedicationRepository.deleteAll()
        caretakerRepository.deleteAll()

        for (i in 1..3) {
            val caretaker = Caretaker(
                username = "CtUsername$i",
                loginId = "CtLoginId$i",
                password = "CtPassword$i",
                email = "CtEmail$i",
                phoneNumber = "CtPhoneNumber$i"
            )
            caretakerRepository.save(caretaker)

            val userMedication = UserMedication(
                name = "Medication $i",
                frequency = Frequency.ONCE_A_DAY,
                type = MedicationType.MEDICATION,
                stock = 10,
                expirationDate = fixedTime.plusMonths(1),
                startDate = fixedTime,
                endDate = fixedTime.plusWeeks(1),
                caretaker = caretaker
            )
            userMedicationRepository.save(userMedication)

            val caregiver = Caregiver(
                username = "CgUsername$i",
                loginId = "CgLoginId$i",
                password = "CgPassword$i",
                email = "CgEmail$i",
                phoneNumber = "CgPhoneNumber$i"
            )
            caregiverRepository.save(caregiver)

            val caretakerCaregiver = CaretakerCaregiver(caretaker = caretaker, caregiver = caregiver)
            caretakerCaregiverRepository.save(caretakerCaregiver)
        }
    }

    @Nested
    @DisplayName("알림 생성 테스트")
    inner class CreateNotificationsTests {

        @Test
        @DisplayName("성공 - 알림 생성 후 NotificationDTO 리스트 반환")
        fun createNotifications() {
            //given
            val userMedicationId = userMedicationRepository.findAll().first().id ?: throw IllegalStateException()

            //when
            val notificationDTOS = notificationService.createNotifications(userMedicationId)

            //then
            assertNotNull(notificationDTOS)
            assertTrue(notificationDTOS.isNotEmpty())
            assertEquals(7, notificationDTOS.size)
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 약 ID로 알림 생성 시 예외 발생")
        fun createNotifications_Exception() {
            //given
            val userMedicationId = 999L

            //when&then
            val exception = assertThrows<PillBuddyCustomException> {
                notificationService.createNotifications(userMedicationId)
            }
            assertEquals(ErrorCode.MEDICATION_NOT_FOUND, exception.errorCode)
        }
    }

    @Nested
    @DisplayName("알림 전송 테스트")
    inner class SendNotificationsTests {
        @Test
        @DisplayName("성공 - 알림 전송 성공")
        fun sendNotifications() {
            //given
            val userMedicationId = userMedicationRepository.findAll().first().id ?: throw IllegalStateException()
            notificationService.createNotifications(userMedicationId)

            val caretaker = caretakerRepository.findAll().first()
            val medicationName = userMedicationRepository.findAll().first().name
            val userName = caretaker.username
            val caregiver = caregiverRepository.findAll().first()

            //when
            notificationService.sendNotifications(fixedTime)

            //then
            verify(smsProvider, times(1)).sendNotification(caretaker.phoneNumber, medicationName, userName)
            verify(smsProvider, times(1)).sendNotification(caregiver.phoneNumber, medicationName, userName)
        }

        @Test
        @DisplayName("실패 - 현재 시간에 알림이 존재하지 않아 전송하지 않음")
        fun sendNotifications_NoNotifications() {
            // given
            val noNotificationsTime = fixedTime.plusHours(1)

            val userMedicationId = userMedicationRepository.findAll().first().id ?: throw IllegalStateException()
            notificationService.createNotifications(userMedicationId)

            val caretaker = caretakerRepository.findAll().first()
            val medicationName = userMedicationRepository.findAll().first().name
            val userName = caretaker.username

            // when
            notificationService.sendNotifications(noNotificationsTime)

            // then
            verify(smsProvider, never()).sendNotification(caretaker.phoneNumber, medicationName, userName)
        }

        @Test
        @DisplayName("실패 - 알림 전송 실패 시 예외 발생")
        fun sendNotifications_CaretakerSendFailure() {
            // given
            val userMedicationId = userMedicationRepository.findAll().first().id ?: throw IllegalStateException()
            notificationService.createNotifications(userMedicationId)

            val caretaker = caretakerRepository.findAll().first()
            val medicationName = userMedicationRepository.findAll().first().name
            val userName = caretaker.username

            doThrow(RuntimeException("SMS 전송 실패")).whenever(smsProvider).sendNotification(caretaker.phoneNumber, medicationName, userName)

            // when & then
            val exception = assertThrows<PillBuddyCustomException> {
                notificationService.sendNotifications(fixedTime)
            }
            assertEquals(ErrorCode.MESSAGE_SEND_FAILED, exception.errorCode)
        }
    }

    @Nested
    @DisplayName("약 복용 확인 알림 테스트")
    inner class CheckAndSendForMissedMedicationsTests {

        @Test
        @DisplayName("성공 - 약 복용 알림 전송")
        fun sendForMissedMedications() {
            // given
            val userMedication = userMedicationRepository.findAll().first()
            val userMedicationId = userMedication.id ?: throw IllegalStateException()
            notificationService.createNotifications(userMedicationId)

            val caretaker = caretakerRepository.findAll().first()
            val caregiver = caregiverRepository.findAll().first()

            // when
            notificationService.checkAndSendForMissedMedications(fixedTime.plusMinutes(15))

            // then
            verify(smsProvider, times(1)).sendCheckNotification(
                caregiver.phoneNumber,
                userMedication.name,
                caretaker.username
            )
        }

        @Test
        @DisplayName("실패 - 약 복용 알림이 전송되지 않음")
        fun sendForMissedMedications_NoNotificationSent() {
            // given
            val userMedication = userMedicationRepository.findAll().first()
            val userMedicationId = userMedication.id ?: throw IllegalStateException()
            notificationService.createNotifications(userMedicationId)

            val caretaker = caretakerRepository.findAll().first()
            val caregiver = caregiverRepository.findAll().first()

            // when
            notificationService.checkAndSendForMissedMedications(fixedTime.minusDays(1))

            // then
            verify(smsProvider, never()).sendCheckNotification(
                caregiver.phoneNumber,
                userMedication.name,
                caretaker.username)
        }
    }
}