package medinine.pill_buddy.global.exception

import org.springframework.http.HttpStatus

data class ErrorResponse(
    val httpStatus: HttpStatus,
    val message: String
) {
    companion object {
        fun of(httpStatus: HttpStatus, message: String): ErrorResponse {
            return ErrorResponse(httpStatus, message)
        }
    }
}