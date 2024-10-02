package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.testing.ability.SetNextAccountIdAbility
import camilyed.github.io.currencyexchangeapi.testing.ability.SetNextAccountIdAbility.Companion.accountRepository
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasId
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasInitialBalance
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasOwner
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountCommandBuilder
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountCommandBuilder.Companion.anAccount
import org.junit.jupiter.api.Test
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure
import strikt.assertions.message
import java.math.BigDecimal

class AccountServiceTest : SetNextAccountIdAbility {

    private val accountService = AccountService(accountRepository)

    @Test
    fun `should create a new account with valid details`() {
        // given
        theNextAccountIdWillBe("db59d3ba-5044-4ea9-85e2-aa1e67ec713c")

        // when
        val account = create(
            anAccount()
                .withOwner("Jan Kowalski")
                .withInitialBalance(BigDecimal(1000.0))
        )

        // then
        expectThat(account)
            .hasId("db59d3ba-5044-4ea9-85e2-aa1e67ec713c")
            .hasOwner("Jan Kowalski")
            .hasInitialBalance(1000.0)
    }

    @Test
    fun `should throw exception when trying to create account with negative balance`() {
        // when
        val result = expectCatching {
            create(anAccount().withInitialBalance(BigDecimal(-1000.0)))
        }

        // then
        result
            .isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Initial balance cannot be negative")
    }

    private fun create(command: CreateAccountCommandBuilder): Account {
        return accountService.create(command.build())
    }
}
