package camilyed.github.io.currencyexchangeapi.testing.ability

import camilyed.github.io.currencyexchangeapi.testing.fakes.TestingAccountRepository
import java.util.*

interface SetNextAccountIdAbility {

    val accountRepository: TestingAccountRepository

    fun theNextAccountIdWillBe(id: String) {
        accountRepository.setNextId(UUID.fromString(id))
    }
}
