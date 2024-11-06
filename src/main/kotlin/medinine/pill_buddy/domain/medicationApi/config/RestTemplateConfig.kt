package medinine.pill_buddy.domain.medicationApi.config

import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.core5.http.io.SocketConfig
import org.apache.hc.core5.util.Timeout
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.time.Duration
import java.util.function.Supplier

@Configuration
class RestTemplateConfig {
    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
        // 커넥션 풀 설정
        val connectionManager = PoolingHttpClientConnectionManager().apply {
            // 최대 연결 수 및 경로당 최대 연결 수 설정
            maxTotal = 100
            defaultMaxPerRoute = 20
            // ConnectionConfig로 연결 타임아웃 설정
            setDefaultConnectionConfig(ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(3)) // 연결 타임아웃
                .build())
            // SocketConfig로 소켓 응답 타임아웃 설정
            defaultSocketConfig = SocketConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(3))         // 응답 타임아웃
                .build()
        }


        // HttpClient 생성 및 타임아웃 설정
        val httpClient = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setResponseTimeout(Timeout.of(Duration.ofSeconds(3))) // 응답 타임아웃
                    .build()
            )
            .build()

        // HttpComponentsClientHttpRequestFactory에 HttpClient 설정
        val factory = HttpComponentsClientHttpRequestFactory(httpClient)

        return restTemplateBuilder
            .requestFactory(Supplier { factory })
            .build()
    }
}