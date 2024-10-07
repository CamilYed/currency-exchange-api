package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.common.Money
import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountNotFoundException
import camilyed.github.io.currencyexchangeapi.domain.AccountOperationRepository
import camilyed.github.io.currencyexchangeapi.domain.AccountRepository
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import camilyed.github.io.currencyexchangeapi.domain.CreateAccountData
import camilyed.github.io.currencyexchangeapi.domain.CurrentExchangeRateProvider
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class AccountService(
    private val repository: AccountRepository,
    private val currentExchangeRateProvider: CurrentExchangeRateProvider,
    private val accountOperationRepository: AccountOperationRepository,
) {

    fun create(command: CreateAccountCommand): AccountSnapshot {
        val accountId = accountOperationRepository.findAccountIdBy(command.commandId)
        if (accountId != null) {
            return transaction { findAccount(accountId).toSnapshot() }
        }
        val id = repository.nextAccountId()
        val account = Account.createNewAccount(command.toCreateAccountData(id))
        inTransaction {
            repository.save(account)
            val events = account.getEvents()
            accountOperationRepository.save(events)
        }
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
        repository.find(id) ?: throw AccountNotFoundException("Account with id $id not found")

    private fun CreateAccountCommand.toCreateAccountData(id: UUID) = CreateAccountData(
        id = id,
        owner = this.owner,
        initialBalancePln = this.initialBalance,
        operationId = this.commandId,
    )
}
