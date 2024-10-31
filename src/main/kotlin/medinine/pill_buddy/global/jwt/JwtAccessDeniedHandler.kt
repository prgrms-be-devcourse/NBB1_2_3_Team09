package medinine.pill_buddy.global.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import medinine.pill_buddy.global.exception.ErrorResponse
import medinine.pill_buddy.log
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAccessDeniedHandler(
    private var objectMapper: ObjectMapper
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: AccessDeniedException
    ) {
        log.error("No Authorities", e);

        val errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, e.message!!)
        response.status = HttpStatus.FORBIDDEN.value();
        response.contentType = "application/json";
        response.characterEncoding = "UTF-8";
        response.writer.write(objectMapper.writeValueAsString(errorResponse));
    }
}