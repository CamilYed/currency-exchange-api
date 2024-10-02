package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountRepository
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import java.math.BigDecimal

class AccountService(
    private val repository: AccountRepository
) {

    fun create(command: CreateAccountCommand): AccountSnapshot {
        val id = repository.nextAccountId()
        return Account(
            id = id,
            owner = command.owner,
            balancePln = command.initialBalance,
            balanceUsd = BigDecimal.ZERO
        ).toSnapshot()
    }
}
