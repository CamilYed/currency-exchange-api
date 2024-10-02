package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.testing.ability.SetNextAccountIdAbility
import camilyed.github.io.currencyexchangeapi.testing.ability.SetNextAccountIdAbility.Companion.accountRepository
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasId
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasInitialBalance
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasOwner
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountCommandBuilder
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountCommandBuilder.Companion.anAccount
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import java.math.BigDecimal

class AccountServiceTest: SetNextAccountIdAbility {

    private val accountService = AccountService(accountRepository)

    @Test
    fun `should create a new account with valid details`() {
        // given
        theNextAccountIdWillBe("db59d3ba-5044-4ea9-85e2-aa1e67ec713c")

        // and
        val command = anAccount()
            .withOwner("Jan Kowalski")
            .withInitialBalance(BigDecimal(1000.0))

        // when
        val account = createAccount(command)

        // then
        expectThat(account)
            .hasId("db59d3ba-5044-4ea9-85e2-aa1e67ec713c")
            .hasOwner("Jan Kowalski")
            .hasInitialBalance(1000.0)
    }

    @Test
    fun `should throw exception when trying to create account with negative balance`() {
        // given
        val command = anAccount()
            .withInitialBalance(BigDecimal(-1000.0))

        // when
        val exception = assertThrows<IllegalArgumentException> {
            createAccount(command)
        }

        // and
        assertEquals("Initial balance cannot be negative", exception.message)
    }

    private fun createAccount(command: CreateAccountCommandBuilder): Account {
        return accountService.create(command.build())
    }
}
