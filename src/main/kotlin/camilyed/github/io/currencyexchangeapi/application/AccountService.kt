package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountRepository
import java.math.BigDecimal

class AccountService(
    private val repository: AccountRepository
) {

    fun create(command: CreateAccountCommand): Account {
        val id = repository.nextAccountId()
        return Account(
            id = id,
            owner = command.owner,
            balancePln = command.initialBalance,
            balanceUsd = BigDecimal.ZERO
        )
    }
}
