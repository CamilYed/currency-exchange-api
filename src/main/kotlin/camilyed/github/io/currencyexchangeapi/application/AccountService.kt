package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.common.Money
import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountNotFoundException
import camilyed.github.io.currencyexchangeapi.domain.AccountRepository
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import camilyed.github.io.currencyexchangeapi.domain.ExchangeRate
import java.math.BigDecimal
import java.util.UUID

class AccountService(
    private val repository: AccountRepository,
) {
    fun create(command: CreateAccountCommand): AccountSnapshot {
        val id = repository.nextAccountId()
        val account =
            Account(
                id = id,
                owner = command.owner,
                balancePln = Money.pln(command.initialBalance),
                balanceUsd = Money.usd(BigDecimal.ZERO),
            )
        repository.save(account)
        return account.toSnapshot()
    }

    fun exchangePlnToUsd(command: ExchangePlnToUsdCommand): AccountSnapshot {
        val account = findAccount(command.accountId)
        account.exchangePlnToUsd(Money.pln(command.amount), ExchangeRate(command.exchangeRate))
        return account.toSnapshot()
    }

    fun exchangeUsdToPln(command: ExchangeUsdToPlnCommand): AccountSnapshot {
        val account = findAccount(command.accountId)
        account.exchangeUsdToPln(Money.usd(command.amount), ExchangeRate(command.exchangeRate))
        return account.toSnapshot()
    }

    private fun findAccount(id: UUID) =
        repository.find(id)
            ?: throw AccountNotFoundException("Account with id $id not found")
}
