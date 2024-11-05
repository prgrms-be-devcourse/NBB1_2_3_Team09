package medinine.pill_buddy.global.config

import feign.FeignException
import feign.Logger
import feign.codec.Encoder
import feign.codec.ErrorDecoder
import feign.form.spring.SpringFormEncoder
import medinine.pill_buddy.PillBuddyApplication
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackageClasses = [PillBuddyApplication::class])
class FeignConfig {

    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }

    @Bean
    @ConditionalOnMissingBean(ErrorDecoder::class)
    fun commonFeignErrorDecoder(): ErrorDecoder {
        return ErrorDecoder { methodKey, response ->
            if (response.status() >= 400) {
                when (response.status()) {
                    401 -> throw PillBuddyCustomException(ErrorCode.SOCIAL_UNAUTHORIZED)
                    403 -> throw PillBuddyCustomException(ErrorCode.SOCIAL_FORBIDDEN)
                    419 -> throw PillBuddyCustomException(ErrorCode.SOCIAL_EXPIRED_TOKEN)
                    else -> throw PillBuddyCustomException(ErrorCode.SOCIAL_BAD_REQUEST)
                }
            }
            FeignException.errorStatus(methodKey, response)
        }
    }

    @Bean
    fun encoder(converters: ObjectFactory<HttpMessageConverters>): Encoder {
        return SpringFormEncoder(SpringEncoder(converters))
    }
}