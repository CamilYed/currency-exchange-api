package camilyed.github.io.currencyexchangeapi.api

import camilyed.github.io.currencyexchangeapi.application.AccountService
import camilyed.github.io.currencyexchangeapi.application.CreateAccountCommand
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
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
        @RequestBody request: CreateAccountJson,
    ): ResponseEntity<AccountSnapshot> {
        val account = accountService.create(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED).body(account)
    }

    data class CreateAccountJson(
        val owner: String,
        val initialBalance: String,
    )

    private fun CreateAccountJson.toCommand() =
        CreateAccountCommand(
            owner = this.owner,
            initialBalance = BigDecimal(this.initialBalance),
        )
}
