package medinine.pill_buddy.global.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.log
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthenticationFilter(
    private var jwtTokenProvider : JwtTokenProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        try {
            val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION)
            val token: String? = jwtTokenProvider.resolveToken(bearerToken)

            // token 유효성 검증
            if (token != null && jwtTokenProvider.validateToken(token) && jwtTokenProvider.isAccessToken(token)) {
                // token 의 loginId 를 통해 권한 정보를 조회하여 SecurityContext 에 저장
                val authentication: Authentication = jwtTokenProvider.getAuthenticationByToken(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: PillBuddyCustomException) {
            log.error(e.message)
        }
        chain.doFilter(request, response)
    }
}