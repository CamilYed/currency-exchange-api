package camilyed.github.io.currencyexchangeapi.infrastructure

import camilyed.github.io.currencyexchangeapi.domain.InsecureOperationException
import camilyed.github.io.currencyexchangeapi.domain.InsufficientFundsException
import camilyed.github.io.currencyexchangeapi.domain.InvalidAmountException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
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

    @ExceptionHandler(InvalidAmountException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleInvalidAmountException(ex: InvalidAmountException): ResponseEntity<ProblemDetails> {
        val problemDetails = ProblemDetails(
            title = "Invalid Amount",
            status = HttpStatus.UNPROCESSABLE_ENTITY.value(),
            detail = "amount: " + ex.message,
            instance = null,
        )
        return ResponseEntity(problemDetails, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(InsufficientFundsException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleInsufficientFunds(ex: InsufficientFundsException): ResponseEntity<ProblemDetails> {
        val problemDetails = ProblemDetails(
            title = "Insufficient funds",
            status = HttpStatus.UNPROCESSABLE_ENTITY.value(),
            detail = "balance: " + ex.message,
            instance = null,
        )
        return ResponseEntity(problemDetails, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(InsecureOperationException::class)
    fun handleInsecureOperation(
        ex: InsecureOperationException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetails> {
        val problemDetails = ProblemDetails(
            title = "Insecure Operation",
            status = HttpStatus.BAD_REQUEST.value(),
            detail = "X-Request-Id: ${ex.message}",
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
