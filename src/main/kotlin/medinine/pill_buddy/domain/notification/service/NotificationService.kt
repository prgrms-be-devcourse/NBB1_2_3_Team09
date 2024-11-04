package medinine.pill_buddy.domain.notification.service

import medinine.pill_buddy.domain.notification.dto.NotificationDTO
import medinine.pill_buddy.domain.notification.dto.UpdateNotificationDTO
import medinine.pill_buddy.domain.notification.entity.Notification
import medinine.pill_buddy.domain.notification.provider.SmsProvider
import medinine.pill_buddy.domain.notification.repository.NotificationRepository
import medinine.pill_buddy.domain.record.entity.Record
import medinine.pill_buddy.domain.record.entity.Taken
import medinine.pill_buddy.domain.user.caretaker.entity.CaretakerCaregiver
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerCaregiverRepository
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.entity.UserMedication
import medinine.pill_buddy.domain.userMedication.repository.UserMedicationRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.log
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
@Transactional
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val userMedicationRepository: UserMedicationRepository,
    private val caretakerCaregiverRepository: CaretakerCaregiverRepository,
    private val smsProvider: SmsProvider,
    private val caretakerRepository: CaretakerRepository
) {
    // 주어진 사용자 약 ID로부터 약을 찾아 알림을 생성합니다.
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
    fun sendNotifications(currentTime: LocalDateTime) {
        val nowPlusOneMinute = currentTime.plusMinutes(1)

        val notifications = notificationRepository.findByNotificationTime(currentTime, nowPlusOneMinute)
        if (notifications.isNotEmpty()) {
            log.info("현재 시간: $currentTime. 등록된 알림 개수: ${notifications.size}. 알림을 처리 중입니다.")
            notifications.forEach {
                sendNotificationToCaretaker(it)
                sendNotificationToCaregivers(it)
            }
        } else {
            log.info("현재 시간: $currentTime. 등록된 알림이 없습니다.")
        }
    }

    private fun sendNotificationToCaretaker(notification: Notification) {
        val phoneNumber = notification.caretaker?.phoneNumber ?: throw PillBuddyCustomException(ErrorCode.PHONE_NUMBER_NOT_FOUND)
        val (medicationName, userName) = getMedicationAndUserName(notification)

        try {
            smsProvider.sendNotification(phoneNumber, medicationName, userName)
            log.info("Caretaker에게 메세지를 전송하였습니다.")
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
                    log.info("Caregiver에게 메세지를 전송하였습니다.")
                } catch (e: Exception) {
                    throw PillBuddyCustomException(ErrorCode.MESSAGE_SEND_FAILED)
                }
            }
        }
    }

    // 약 복용 확인 메세지를 전송합니다.
    fun checkAndSendForMissedMedications(currentTime: LocalDateTime) {
        log.info("현재 시간: $currentTime. 사용자가 약을 복용 했는지 확인합니다.")
        val userMedications = userMedicationRepository.findAll()

        userMedications.forEach { checkRecords(it, currentTime) }
    }

    private fun checkRecords(userMedication: UserMedication, currentTime: LocalDateTime) {
        val notifications = userMedication.notificationList
        val records = userMedication.records

        notifications.forEach {
            if (!hasNotificationTimePassed(it.notificationTime, currentTime)) return@forEach

            if (!wasTaken(it.notificationTime, records) && isTimeToNotify(it.notificationTime, currentTime)) {
                log.info("사용자가 약을 복용하지 않은 채 15분이 지났습니다.")
                sendMissedNotification(userMedication, it)
            }
        }
    }

    private fun hasNotificationTimePassed(notificationTime: LocalDateTime, currentTime: LocalDateTime): Boolean {
        return currentTime.isAfter(notificationTime)
    }

    private fun wasTaken(notificationTime: LocalDateTime, records: MutableList<Record>): Boolean {
        return records.any { record ->
            record.date.isAfter(notificationTime) && record.taken == Taken.TAKEN
        }
    }

    private fun isTimeToNotify(notificationTime: LocalDateTime, currentTime: LocalDateTime): Boolean {
        val fewMinutesAfter = notificationTime.plusMinutes(15)
        return currentTime.truncatedTo(ChronoUnit.MINUTES) == fewMinutesAfter.truncatedTo(ChronoUnit.MINUTES)
    }

    private fun sendMissedNotification(userMedication: UserMedication, notification: Notification) {
        val caretakerCaregivers = findCaretakerCaregivers(userMedication)

        if (caretakerCaregivers.isNotEmpty()) {
            sendNotificationToCaregivers(caretakerCaregivers, notification)
        } else {
            throw PillBuddyCustomException(ErrorCode.CAREGIVER_NOT_FOUND)
        }
    }

    private fun findCaretakerCaregivers(userMedication: UserMedication): List<CaretakerCaregiver> {
        val caretaker = userMedication.caretaker ?: throw PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND)
        return caretakerCaregiverRepository.findByCaretaker(caretaker)
    }

    private fun sendNotificationToCaregivers(caretakerCaregivers: List<CaretakerCaregiver>, notification: Notification) {
        val (medicationName, userName) = getMedicationAndUserName(notification)

        caretakerCaregivers.forEach {
            val caregiverPhoneNumber = it.caregiver?.phoneNumber ?: throw PillBuddyCustomException(ErrorCode.PHONE_NUMBER_NOT_FOUND)
            try {
                smsProvider.sendCheckNotification(caregiverPhoneNumber, medicationName, userName)
                log.info("Caregiver에게 메세지를 전송하였습니다.")
            } catch (e: Exception) {
                throw PillBuddyCustomException(ErrorCode.MESSAGE_SEND_FAILED)
            }
        }
    }

    private fun getMedicationAndUserName(notification: Notification): Pair<String, String> {
        val medicationName = notification.userMedication?.name ?: throw PillBuddyCustomException(ErrorCode.MEDICATION_NAME_NOT_FOUND)
        val userName = notification.caretaker?.username ?: throw PillBuddyCustomException(ErrorCode.USER_NAME_NOT_FOUND)
        return Pair(medicationName, userName)
    }

    // 주어진 사용자 ID로부터 알림을 조회합니다.
    fun findNotification(caretakerId: Long): List<NotificationDTO> {
        val caretaker = caretakerRepository.findById(caretakerId)
            .orElseThrow { PillBuddyCustomException(ErrorCode.CARETAKER_NOT_FOUND) }

        return notificationRepository.findByCaretaker(caretaker)
    }

    // 알림 시간을 수정합니다.
    fun updateNotification(notificationId: Long, updateNotification: UpdateNotificationDTO): NotificationDTO {
        val notification = notificationRepository.findById(notificationId)
            .orElseThrow { PillBuddyCustomException(ErrorCode.NOTIFICATION_NOT_FOUND) }

        val notificationTime = updateNotification.notificationTime
        notification.changeNotificationTime(notificationTime)

        return NotificationDTO.convertToDTO(notification)
    }

    // 알림을 삭제합니다.
    fun deleteNotification(notificationId: Long) {
        val notification = notificationRepository.findById(notificationId)
            .orElseThrow { PillBuddyCustomException(ErrorCode.NOTIFICATION_NOT_FOUND) }

        notificationRepository.delete(notification)
    }
}