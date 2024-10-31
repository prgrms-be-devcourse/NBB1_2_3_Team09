package medinine.pill_buddy.domain.notification.service

import medinine.pill_buddy.domain.notification.dto.NotificationDTO
import medinine.pill_buddy.domain.notification.entity.Notification
import medinine.pill_buddy.domain.notification.provider.SmsProvider
import medinine.pill_buddy.domain.notification.repository.NotificationRepository
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerCaregiverRepository
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.entity.UserMedication
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.log
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val userMedicationRepository: UserMedicationRepository,
    private val caretakerCaregiverRepository: CaretakerCaregiverRepository,
    private val smsProvider: SmsProvider
) {
    // 주어진 사용자 약물 ID로부터 약물을 찾아 알림을 생성합니다.
    fun createNotifications(userMedicationId: Long): List<NotificationDTO> {
        val userMedication = userMedicationRepository.findById(userMedicationId)
            .orElseThrow { PillBuddyCustomException(ErrorCode.MEDICATION_NOT_FOUND) }

        val notifications = generateNotifications(userMedication)
        return NotificationDTO.convertToDTOs(notifications)
    }

    private fun generateNotifications(userMedication: UserMedication): List<Notification> {
        val notifications = mutableListOf<Notification>()
        var currentTime = userMedication.startDate

        while (currentTime.isBefore(userMedication.endDate)) {
            val notification = Notification(
                notificationTime = currentTime,
                userMedication = userMedication,
                caretaker = userMedication.caretaker
            )
            notifications.add(notification)
            currentTime = incrementTime(currentTime, userMedication.frequency)
        }
        return notificationRepository.saveAll(notifications)
    }

    private fun incrementTime(time: LocalDateTime, frequency: Frequency): LocalDateTime {
        return when (frequency) {
            Frequency.ONCE_A_DAY -> time.plusDays(1)
            Frequency.TWICE_A_DAY -> time.plusHours(12)
            Frequency.THREE_TIMES_A_DAY -> time.plusHours(8)
            Frequency.WEEKLY -> time.plusWeeks(1)
            Frequency.BIWEEKLY -> time.plusWeeks(2)
            Frequency.MONTHLY -> time.plusMonths(1)
        }
    }

    // 현재 시간과 1분 후의 시간을 계산하여 등록된 알림을 조회 후 전송합니다.
    fun sendNotifications() {
        val now = LocalDateTime.now()
        val nowPlusOneMinute = now.plusMinutes(1)

        val notifications = notificationRepository.findByNotificationTime(now, nowPlusOneMinute)
        if (notifications.isNotEmpty()) {
            log.info("현재 시간: $now. 등록된 알림 개수: ${notifications.size}. 알림을 처리 중입니다.")
            notifications.forEach { notification ->
                sendNotificationToCaretaker(notification)
                sendNotificationToCaregivers(notification)
                notificationRepository.delete(notification)
            }
        } else {
            log.info("현재 시간: $now. 등록된 알림이 없습니다.")
        }
    }

    private fun sendNotificationToCaretaker(notification: Notification) {
        val phoneNumber = notification.caretaker?.phoneNumber ?: throw PillBuddyCustomException(ErrorCode.PHONE_NUMBER_NOT_FOUND)
        val (medicationName, userName) = getMedicationAndUserName(notification)

        try {
            smsProvider.sendNotification(phoneNumber, medicationName, userName)
            log.info("Caretaker에게 메세지 전송 성공")
        } catch (e: Exception) {
            throw PillBuddyCustomException(ErrorCode.MESSAGE_SEND_FAILED)
        }
    }

    private fun sendNotificationToCaregivers(notification: Notification) {
        val caretakerCaregivers = caretakerCaregiverRepository.findByCaretaker(notification.caretaker!!)
        if (caretakerCaregivers.isNotEmpty()) {
            val (medicationName, userName) = getMedicationAndUserName(notification)

            caretakerCaregivers.forEach { caretakerCaregiver ->
                val caregiverPhoneNumber = caretakerCaregiver.caregiver?.phoneNumber ?: throw PillBuddyCustomException(ErrorCode.PHONE_NUMBER_NOT_FOUND)
                try {
                    smsProvider.sendNotification(caregiverPhoneNumber, medicationName, userName)
                    log.info("Caregiver에게 메세지 전송 성공")
                } catch (e: Exception) {
                    throw PillBuddyCustomException(ErrorCode.MESSAGE_SEND_FAILED)
                }
            }
        }
    }

    private fun getMedicationAndUserName(notification: Notification): Pair<String, String> {
        val medicationName = notification.userMedication?.name ?: throw PillBuddyCustomException(ErrorCode.MEDICATION_NAME_NOT_FOUND)
        val userName = notification.caretaker?.username ?: throw PillBuddyCustomException(ErrorCode.USER_NAME_NOT_FOUND)
        return Pair(medicationName, userName)
    }
}