package medinine.pill_buddy.global.config

import medinine.pill_buddy.global.jwt.JwtAccessDeniedHandler
import medinine.pill_buddy.global.jwt.JwtAuthenticationEntryPoint
import medinine.pill_buddy.global.jwt.JwtAuthenticationFilter
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private var authenticationFilter: JwtAuthenticationFilter,
    private var jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private var entryPoint: JwtAuthenticationEntryPoint
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            // rest api 사용으로 basic auth 및 csrf 보안 X
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            // 예외를 사용자에게 전달하기 위한 entryPoint, exceptionHandler 설정
            .exceptionHandling { ex ->
                ex.accessDeniedHandler(jwtAccessDeniedHandler)
                ex.authenticationEntryPoint(entryPoint)
            }
            // jwt 토큰을 사용하기 때문에 세션 사용 X
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { req ->
                req
                    .requestMatchers(
                        // 로그인 관련
                        HttpMethod.POST,
                        "/api/users/join",
                        "/api/users/login",
                        "/api/users/reissue-token",
                    ).permitAll()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/api/users/oauth/**",
                        "/api/search",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                    ).permitAll()
                    .requestMatchers(
                        "/search/**",
                        "/error"
                    ).permitAll()
                    .anyRequest().hasRole("USER")
            }
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}