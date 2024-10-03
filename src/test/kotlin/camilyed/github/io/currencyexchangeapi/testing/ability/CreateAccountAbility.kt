package camilyed.github.io.currencyexchangeapi.testing.ability

import camilyed.github.io.common.Money
import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import camilyed.github.io.currencyexchangeapi.testing.builders.AccountSnapshotBuilder
import camilyed.github.io.currencyexchangeapi.testing.fakes.TestingAccountRepository

interface CreateAccountAbility {
    val accountRepository: TestingAccountRepository

    fun thereIsAnAccount(builder: AccountSnapshotBuilder): AccountSnapshot {
        val snapshot = builder.build()
        val account = Account(
            id = snapshot.id,
            owner = snapshot.owner,
            balancePln = Money.pln(snapshot.balancePln),
            balanceUsd = Money.usd(snapshot.balanceUsd)
        )
        accountRepository.save(account)
        return account.toSnapshot()
    }
}
