package camilyed.github.io.currencyexchangeapi.api

import camilyed.github.io.currencyexchangeapi.application.AccountService
import camilyed.github.io.currencyexchangeapi.application.CreateAccountCommand
import camilyed.github.io.currencyexchangeapi.application.ExchangePlnToUsdCommand
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import camilyed.github.io.currencyexchangeapi.domain.OperationId
import camilyed.github.io.currencyexchangeapi.infrastructure.InvalidHeaderException
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/api/accounts")
class AccountEndpoint(private val accountService: AccountService) {

    @PostMapping
    fun createAccount(
        @Valid @RequestBody request: CreateAccountJson,
        @RequestHeader("X-Request-Id") requestId: XRequestId?,
    ): ResponseEntity<AccountCreatedJson> {
        if (requestId == null) {
            throw InvalidHeaderException("X-Request-Id is required and must be a valid UUID")
        }
        val account = accountService.create(request.toCommand(requestId.toOperationId()))
        return ResponseEntity.status(HttpStatus.CREATED).body(account.toAccountCreatedJson())
    }

    @PutMapping("/exchange-pln-to-usd")
    fun exchangePlnToUsd(
        @Valid @RequestBody request: ExchangePlnToUsdJson,
        @RequestHeader("X-Request-Id") requestId: String?,
    ): ResponseEntity<AccountSnapshotJson> {
        if (requestId == null) {
            throw InvalidHeaderException("X-Request-Id is required and must be a valid UUID")
        }
        val command = request.toCommand(requestId.toOperationId())
        val account = accountService.exchangePlnToUsd(command)
        return ResponseEntity.ok(account.toAccountSnapshotJson())
    }

    data class ExchangePlnToUsdJson(
        @field:NotBlank(message = "Account ID cannot be blank")
        val accountId: String?,

        @field:NotBlank(message = "Amount cannot be blank")
        @field:Pattern(
            regexp = "\\d+(\\.\\d{1,2})?",
            message = "Amount must be a valid decimal number",
        )
        val amount: String?,
    )

    data class CreateAccountJson(
        @field:NotBlank(message = "Owner cannot be blank")
        val owner: String?,

        @field:NotBlank(message = "Initial balance cannot be blank")
        @field:Pattern(
            regexp = "\\d+(\\.\\d{1,2})?",
            message = "Initial balance must be a valid decimal number",
        )
        val initialBalance: String?,
    )

    data class AccountCreatedJson(
        val id: String,
    )

    private fun CreateAccountJson.toCommand(operationId: OperationId) =
        CreateAccountCommand(
            owner = this.owner!!,
            initialBalance = BigDecimal(this.initialBalance),
            operationId,
        )

    private fun ExchangePlnToUsdJson.toCommand(operationId: OperationId) = ExchangePlnToUsdCommand(
        accountId = UUID.fromString(this.accountId),
        amount = BigDecimal(this.amount),
        operationId = operationId,
    )

    private fun AccountSnapshot.toAccountCreatedJson() = AccountCreatedJson(this.id.toString())

    private fun AccountSnapshot.toAccountSnapshotJson() = AccountSnapshotJson(
        id = this.id.toString(),
        owner = this.owner,
        balancePln = this.balancePln.toString(),
        balanceUsd = this.balanceUsd.toString(),
    )

    data class AccountSnapshotJson(
        val id: String,
        val owner: String,
        val balancePln: String,
        val balanceUsd: String,
    )
}
