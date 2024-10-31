package medinine.pill_buddy.domain.notification.provider

import medinine.pill_buddy.log
import net.nurigo.sdk.NurigoApp
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component

@Component
@EnableScheduling
class SmsProvider(
    @Value("\${sms.api-key}") API_KEY: String,
    @Value("\${sms.api-secret-key}") API_SECRET_KEY: String,
    @Value("\${sms.domain}") DOMAIN: String,
    @Value("\${sms.from-number}") private val FROM: String
) {
    private val messageService: DefaultMessageService = NurigoApp.initialize(API_KEY, API_SECRET_KEY, DOMAIN)

    fun sendNotification(phoneNumber: String, medicationName: String, userName: String) {
        val message = Message().apply {
            from = FROM
            to = phoneNumber
            text = "$userName 님 약 복용 시간입니다: $medicationName"
        }

        val response = messageService.sendOne(SingleMessageSendingRequest(message))
        log.info("메세지 전송 성공: $response")
    }
}