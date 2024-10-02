package camilyed.github.io.currencyexchangeapi.testing.ability

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountCommandBuilder
import camilyed.github.io.currencyexchangeapi.testing.fakes.TestingAccountRepository
import java.math.BigDecimal

interface CreateAccountAbility {
    val accountRepository: TestingAccountRepository

    fun thereIsAnAccount(command: CreateAccountCommandBuilder): AccountSnapshot {
        val account = accountRepository.nextAccountId()
        val newAccount = Account(
            id = account,
            owner = command.build().owner,
            balancePln = command.build().initialBalance,
            balanceUsd = BigDecimal.ZERO
        )
        accountRepository.save(newAccount)
        return newAccount.toSnapshot()
    }
}
