package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import camilyed.github.io.currencyexchangeapi.testing.ability.SetNextAccountIdAbility
import camilyed.github.io.currencyexchangeapi.testing.ability.SetNextAccountIdAbility.Companion.accountRepository
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasBalanceInPln
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasBalanceInUsd
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasId
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
                .withInitialBalance("1000.00")
        )

        // then
        expectThat(account)
            .hasId("db59d3ba-5044-4ea9-85e2-aa1e67ec713c")
            .hasOwner("Jan Kowalski")
            .hasBalanceInPln("1000.00")
    }

    @Test
    fun `should throw exception when trying to create account with negative balance`() {
        // when
        val result = expectCatching {
            create(anAccount().withInitialBalance("-1000.00"))
        }

        // then
        result
            .isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Initial balance cannot be negative")
    }

    @Test
    fun `should have zero USD balance before exchange`() {
        // given
        val account = create(anAccount().withInitialBalance("1000.00"))

        // then
        expectThat(account)
            .hasBalanceInPln("1000.00")
            .hasBalanceInUsd("0.00")
    }

    @Test
    fun `should exchange PLN to USD`() {
        // given
        var account = create(anAccount().withInitialBalance("1000.00"))
        val exchangeRate = BigDecimal(4.0) // 1 USD = 4 PLN

        // when
        account = accountService.exchangePlnToUsd(account.id, BigDecimal(400.00), exchangeRate)

        // then
        expectThat(account)
            .hasBalanceInPln("600.00")
            .hasBalanceInUsd("100.00")
    }

    @Test
    fun `should not allow exchange of 0 PLN to USD`() {
        // given
        val account = create(anAccount().withInitialBalance("1000.00"))
        val exchangeRate = BigDecimal(4.0)

        // then
        expectCatching {
            accountService.exchangePlnToUsd(account.id, BigDecimal(0.0), exchangeRate)
        }.isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Amount must be greater than 0")
    }

    private fun create(command: CreateAccountCommandBuilder): AccountSnapshot {
        return accountService.create(command.build())
    }
}
