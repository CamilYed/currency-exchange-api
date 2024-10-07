package camilyed.github.io.currencyexchangeapi.testing.ability

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import camilyed.github.io.currencyexchangeapi.testing.builders.AccountSnapshotBuilder
import camilyed.github.io.currencyexchangeapi.testing.fakes.TestingAccountRepository

interface CreateAccountAbility {
    val accountRepository: TestingAccountRepository

    fun thereIsAnAccount(builder: AccountSnapshotBuilder): AccountSnapshot {
        val snapshot = builder.build()
        val account = Account.fromSnapshot(snapshot)
        accountRepository.save(account)
        return account.toSnapshot()
    }
}
