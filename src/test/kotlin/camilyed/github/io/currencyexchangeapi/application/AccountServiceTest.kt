package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import camilyed.github.io.currencyexchangeapi.testing.ability.CreateAccountAbility
import camilyed.github.io.currencyexchangeapi.testing.ability.SetNextAccountIdAbility
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasBalanceInPln
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasBalanceInUsd
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasId
import camilyed.github.io.currencyexchangeapi.testing.assertions.hasOwner
import camilyed.github.io.currencyexchangeapi.testing.builders.AccountSnapshotBuilder.Companion.anAccount
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountCommandBuilder
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountCommandBuilder.Companion.aCreateAccountCommand
import camilyed.github.io.currencyexchangeapi.testing.builders.ExchangePlnToUsdCommandBuilder
import camilyed.github.io.currencyexchangeapi.testing.builders.ExchangePlnToUsdCommandBuilder.Companion.anExchangeToUsd
import camilyed.github.io.currencyexchangeapi.testing.builders.ExchangeUsdToPlnCommandBuilder
import camilyed.github.io.currencyexchangeapi.testing.builders.ExchangeUsdToPlnCommandBuilder.Companion.anExchangeToPln
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
            aCreateAccountCommand()
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
            create(aCreateAccountCommand().withInitialBalance("-1000.00"))
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
        val account = create(aCreateAccountCommand().withInitialBalance("1000.00"))

        // then
        expectThat(account)
            .hasBalanceInPln("1000.00")
            .hasBalanceInUsd("0.00")
    }

    @Test
    fun `should exchange PLN to USD`() {
        // given
        var account = thereIsAnAccount(anAccount().withBalancePln("1000.00"))

        // when
        account = exchange(
            anExchangeToUsd()
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
            exchange(
                anExchangeToUsd()
                    .withAccountId(account.id)
                    .withAmount("0.00")
            )
        }.isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Amount must be greater than 0")
    }

    @Test
    fun `should not allow a negative PLN amount to be provided for exchange`() {
        // given
        val account = thereIsAnAccount(anAccount())

        // then
        expectCatching {
            exchange(
                anExchangeToUsd()
                    .withAccountId(account.id)
                    .withAmount("-100.00")
            )
        }.isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Amount must be greater than 0")
    }

    @Test
    fun `should exchange USD to PLN`() {
        // given
        var account = thereIsAnAccount(
            anAccount()
                .withBalancePln("600.00")
                .withBalanceUsd("100.00")
        )

        // when
        account = exchange(
            anExchangeToPln()
                .withAccountId(account.id)
                .withAmount("100.00")
                .withExchangeRate("4.0")
        )

        // then
        expectThat(account)
            .hasBalanceInPln("1000.00")
            .hasBalanceInUsd("0.00")
    }

    @Test
    fun `should not allow exchange of 0 USD to PLN`() {
        // given
        val account = thereIsAnAccount(anAccount())

        // then
        expectCatching {
            exchange(
                anExchangeToPln()
                    .withAccountId(account.id)
                    .withAmount("0.00")
            )
        }.isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Amount must be greater than 0")
    }

    @Test
    fun `should not allow a negative USD amount to be provided for exchange`() {
        // given
        val account = thereIsAnAccount(anAccount())

        // then
        expectCatching {
            exchange(
                anExchangeToPln()
                    .withAccountId(account.id)
                    .withAmount("-100.00")
            )
        }.isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Amount must be greater than 0")
    }

    @Test
    fun `should throw exception if PLN balance is insufficient`() {
        // given
        val account = thereIsAnAccount(anAccount().withBalancePln("100.00"))

        // then
        expectCatching {
            exchange(
                anExchangeToUsd()
                    .withAccountId(account.id)
                    .withAmount("200.00")
            )
        }.isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Insufficient PLN balance")
    }

    @Test
    fun `should throw exception if USD balance is insufficient`() {
        // given
        val account = thereIsAnAccount(anAccount().withBalanceUsd("50.00"))

        // then
        expectCatching {
            exchange(
                anExchangeToPln()
                    .withAccountId(account.id)
                    .withAmount("100.00")
            )
        }.isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Insufficient USD balance")
    }

    @Test
    fun `should exchange full PLN balance to USD`() {
        // given
        val account = thereIsAnAccount(anAccount().withBalancePln("1000.00"))

        // when
        val updatedAccount = exchange(
            anExchangeToUsd()
                .withAccountId(account.id)
                .withAmount("1000.00")
                .withExchangeRate("4.0")
        )

        // then
        expectThat(updatedAccount)
            .hasBalanceInPln("0.00")
            .hasBalanceInUsd("250.00")
    }

    private fun create(command: CreateAccountCommandBuilder): AccountSnapshot {
        return accountService.create(command.build())
    }

    private fun exchange(command: ExchangePlnToUsdCommandBuilder): AccountSnapshot {
        return accountService.exchangePlnToUsd(command.build())
    }

    private fun exchange(command: ExchangeUsdToPlnCommandBuilder): AccountSnapshot {
        return accountService.exchangeUsdToPln(command.build())
    }
}
