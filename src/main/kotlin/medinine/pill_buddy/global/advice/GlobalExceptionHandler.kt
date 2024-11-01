package medinine.pill_buddy.global.advice

import medinine.pill_buddy.global.exception.ErrorResponse
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.log
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler
    fun handleCustomException(e: PillBuddyCustomException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(e.errorCode.httpStatus, e.message!!)
        log.error("Error Message: ${e.errorCode.message}")
        return ResponseEntity.status(response.httpStatus).body(response)
    }
}