package medinine.pill_buddy.domain.user.oauth.service

import io.netty.handler.codec.http.HttpHeaderValues
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.dto.UserType.CAREGIVER
import medinine.pill_buddy.domain.user.dto.UserType.CARETAKER
import medinine.pill_buddy.domain.user.oauth.dto.KakaoProfile
import medinine.pill_buddy.domain.user.oauth.dto.KakaoTokenResponse
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.global.oauth.KakaoProperty.KAKAO_AUTHORIZATION_GRANT_TYPE
import medinine.pill_buddy.global.oauth.KakaoProperty.KAKAO_AUTHORIZATION_URI
import medinine.pill_buddy.global.oauth.KakaoProperty.KAKAO_CAREGIVER_REDIRECT_URI
import medinine.pill_buddy.global.oauth.KakaoProperty.KAKAO_CARETAKER_REDIRECT_URI
import medinine.pill_buddy.global.oauth.KakaoProperty.KAKAO_CLIENT_ID
import medinine.pill_buddy.global.oauth.KakaoProperty.KAKAO_OAUTH_QUERY_STRING
import medinine.pill_buddy.global.oauth.KakaoProperty.KAKAO_TOKEN_URI
import medinine.pill_buddy.global.oauth.KakaoProperty.KAKAO_USER_INFO_URI
import medinine.pill_buddy.log
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


@Component
class KakaoClient {

    fun getConnectionUrl(userType: UserType): String {
        val redirectUri = when (userType) {
            CAREGIVER -> KAKAO_CAREGIVER_REDIRECT_URI
            CARETAKER -> KAKAO_CARETAKER_REDIRECT_URI
        }
        return "$KAKAO_AUTHORIZATION_URI${KAKAO_OAUTH_QUERY_STRING.format(KAKAO_CLIENT_ID, redirectUri)}"
    }

    fun getAccessToken(code: String): String {
        val response = WebClient.create(KAKAO_TOKEN_URI).post()
            .uri { uriBuilder ->
                uriBuilder
                    .scheme("https")
                    .queryParam("grant_type", KAKAO_AUTHORIZATION_GRANT_TYPE)
                    .queryParam("client_id", KAKAO_CLIENT_ID)
                    .queryParam("code", code)
                    .build(true)
            }
            .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
            .retrieve()
            .onStatus({ it.is4xxClientError }) { clientResponse ->
                log.error("- getAccessToken() => 4xx Client Error: {}", clientResponse.statusCode())
                Mono.error(RuntimeException("Invalid Parameter"))
            }
            .onStatus({ it.is5xxServerError }) { clientResponse ->
                log.error("- getAccessToken() => 5xx Server Error: {}", clientResponse.statusCode())
                Mono.error(RuntimeException("Internal Server Error"))
            }
            .bodyToMono(KakaoTokenResponse::class.java)
            .block()

        log.info("kakao token response => $response")
        return response?.accessToken ?: throw PillBuddyCustomException(ErrorCode.KAKAO_ACCESS_TOKEN_NOT_FOUND)
    }

    fun getUserInfo(accessToken: String): KakaoProfile {
        val profile = WebClient.create(KAKAO_USER_INFO_URI).get()
            .uri { uriBuilder ->
                uriBuilder.scheme("https").build(true)
            }
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
            .retrieve()
            .onStatus({ it.is4xxClientError }) { clientResponse ->
                log.error("- getUserInfo() => 4xx Client Error: {}", clientResponse.statusCode())
                Mono.error(RuntimeException("Invalid Parameter"))
            }
            .onStatus({ it.is5xxServerError }) { clientResponse ->
                log.error("- getUserInfo() => 5xx Server Error: {}", clientResponse.statusCode())
                Mono.error(RuntimeException("Internal Server Error"))
            }
            .bodyToMono(KakaoProfile::class.java)
            .block()

        log.info("kakao user info => $profile")
        return profile ?: throw PillBuddyCustomException(ErrorCode.KAKAO_USER_INFO_NOT_FOUND)
    }
}