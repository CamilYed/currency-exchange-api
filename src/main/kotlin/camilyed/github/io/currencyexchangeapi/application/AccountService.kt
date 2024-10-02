package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountRepository
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import java.math.BigDecimal
import java.util.*

class AccountService(
    private val repository: AccountRepository
) {

    fun create(command: CreateAccountCommand): AccountSnapshot {
        val id = repository.nextAccountId()
        val account = Account(
            id = id,
            owner = command.owner,
            balancePln = command.initialBalance,
            balanceUsd = BigDecimal.ZERO
        )
        repository.save(account)
        return account.toSnapshot()
    }

    fun exchangePlnToUsd(command: ExchangePlnToUsdCommand): AccountSnapshot {
        val account = repository.find(command.accountId)!!
        account.exchangePlnToUsd(amountPln = command.amount, exchangeRate = command.exchangeRate)
        return account.toSnapshot()
    }
}
