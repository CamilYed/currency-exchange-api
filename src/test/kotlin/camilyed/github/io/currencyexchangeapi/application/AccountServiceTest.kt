package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import camilyed.github.io.currencyexchangeapi.testing.ability.CreateAccountAbility
import camilyed.github.io.currencyexchangeapi.testing.ability.SetNextAccountIdAbility
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasBalanceInPln
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasBalanceInUsd
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasId
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasOwner
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountCommandBuilder
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountCommandBuilder.Companion.anAccount
import camilyed.github.io.currencyexchangeapi.testing.builders.ExchangePlnToUsdCommandBuilder
import camilyed.github.io.currencyexchangeapi.testing.builders.ExchangePlnToUsdCommandBuilder.Companion.anExchange
import camilyed.github.io.currencyexchangeapi.testing.fakes.TestingAccountRepository
import org.junit.jupiter.api.Test
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure
import strikt.assertions.message

class AccountServiceTest : SetNextAccountIdAbility, CreateAccountAbility {

    override val accountRepository = TestingAccountRepository()

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
        var account = thereIsAnAccount(anAccount().withInitialBalance("1000.00"))

        // when
        account = exchangePlnToUsd(
            anExchange()
                .withAccountId(account.id)
                .withAmount("400.00")
                .withExchangeRate("4.0")
        )

        // then
        expectThat(account)
            .hasBalanceInPln("600.00")
            .hasBalanceInUsd("100.00")
    }

    @Test
    fun `should not allow exchange of 0 PLN to USD`() {
        // given
        val account = thereIsAnAccount(anAccount())

        // then
        expectCatching {
            exchangePlnToUsd(
                anExchange()
                    .withAccountId(account.id)
                    .withAmount("0.00")
            )
        }.isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Amount must be greater than 0")
    }

    @Test
    fun `should exchange USD to PLN`() {
        // given
        var account = thereIsAnAccount(anAccount().withInitialBalance("1000.00"))

        // and
        account = exchangePlnToUsd(
            anExchange()
                .withAccountId(account.id)
                .withAmount("400.00")
                .withExchangeRate("4.0")
        )

        // when
        account = exchangeUsdToPln(
            anExchange()
                .withAccountId(account.id)
                .withAmount("100.00")
                .withExchangeRate("4.0")
        )

        // then
        expectThat(account)
            .hasBalanceInPln("1000.00") // Powrót do stanu początkowego
            .hasBalanceInUsd("0.00")    // Saldo USD powinno być teraz zerowe
    }

    private fun create(command: CreateAccountCommandBuilder): AccountSnapshot {
        return accountService.create(command.build())
    }

    private fun exchangePlnToUsd(command: ExchangePlnToUsdCommandBuilder): AccountSnapshot {
        return accountService.exchangePlnToUsd(command.build())
    }
}
