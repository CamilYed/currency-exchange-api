package camilyed.github.io.currencyexchangeapi.infrastructure

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException,
    ): ResponseEntity<ProblemDetails> {
        val errors = ex.bindingResult.allErrors.map { error ->
            (error as FieldError).field + ": " + error.defaultMessage
        }

        val problemDetails = ProblemDetails(
            title = "Validation Failed",
            status = HttpStatus.BAD_REQUEST.value(),
            detail = errors.joinToString(", "),
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetails)
    }

    @ExceptionHandler(InvalidHeaderException::class)
    fun handleInvalidHeaderException(
        ex: InvalidHeaderException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetails> {
        val problemDetails = ProblemDetails(
            title = "Invalid Header",
            status = HttpStatus.BAD_REQUEST.value(),
            detail = "X-Request-Id: " + ex.message,
            instance = request.requestURI,
        )
        return ResponseEntity(problemDetails, HttpStatus.BAD_REQUEST)
    }
}

data class ProblemDetails(
    val title: String,
    val status: Int,
    val detail: String,
    val instance: String? = null,
)
// TODO rozbudowac o sekcje np. invalidFields jako liste
