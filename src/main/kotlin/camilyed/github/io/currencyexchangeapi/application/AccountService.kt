package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.common.Money
import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountNotFoundException
import camilyed.github.io.currencyexchangeapi.domain.AccountRepository
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import camilyed.github.io.currencyexchangeapi.domain.CurrentExchangeRateProvider
import java.math.BigDecimal
import java.util.UUID

class AccountService(
    private val repository: AccountRepository,
    private val currentExchangeRateProvider: CurrentExchangeRateProvider,
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
        inTransaction { repository.save(account) }
        return account.toSnapshot()
    }

    fun exchangePlnToUsd(command: ExchangePlnToUsdCommand): AccountSnapshot {
        val account = findAccount(command.accountId)
        val currentExchange = currentExchangeRateProvider.currentExchange()
        account.exchangePlnToUsd(Money.pln(command.amount), currentExchange)
        return account.toSnapshot()
    }

    fun exchangeUsdToPln(command: ExchangeUsdToPlnCommand): AccountSnapshot {
        val account = findAccount(command.accountId)
        val currentExchange = currentExchangeRateProvider.currentExchange()
        account.exchangeUsdToPln(Money.usd(command.amount), currentExchange)
        return account.toSnapshot()
    }

    private fun findAccount(id: UUID) =
        repository.find(id)
            ?: throw AccountNotFoundException("Account with id $id not found")
}
