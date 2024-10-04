package camilyed.github.io.currencyexchangeapi.api

import camilyed.github.io.currencyexchangeapi.application.AccountService
import camilyed.github.io.currencyexchangeapi.application.CreateAccountCommand
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api/accounts")
class AccountEndpoint(private val accountService: AccountService) {

    @PostMapping
    fun createAccount(
        @Valid @RequestBody request: CreateAccountJson,
    ): ResponseEntity<AccountCreatedJson> {
        val account = accountService.create(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED).body(account.toAccountCreatedJson())
    }

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

    private fun CreateAccountJson.toCommand() =
        CreateAccountCommand(
            owner = this.owner!!,
            initialBalance = BigDecimal(this.initialBalance),
        )

    private fun AccountSnapshot.toAccountCreatedJson() = AccountCreatedJson(this.id.toString())
}
