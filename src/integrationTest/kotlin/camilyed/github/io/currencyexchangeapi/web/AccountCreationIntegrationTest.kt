package camilyed.github.io.currencyexchangeapi.web

import camilyed.github.io.currencyexchangeapi.testing.BaseIntegrationTest
import camilyed.github.io.currencyexchangeapi.testing.abilties.CreateAccountAbility
import camilyed.github.io.currencyexchangeapi.testing.abilties.GetCurrentExchangeRateAbility
import camilyed.github.io.currencyexchangeapi.testing.assertion.hasProblemDetail
import camilyed.github.io.currencyexchangeapi.testing.assertion.hasUUID
import camilyed.github.io.currencyexchangeapi.testing.assertion.isBadRequest
import camilyed.github.io.currencyexchangeapi.testing.assertion.isOkResponse
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountJsonBuilder.Companion.aCreateAccount
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class AccountCreationIntegrationTest :
    BaseIntegrationTest(),
    CreateAccountAbility,
    GetCurrentExchangeRateAbility {

    @Test
    fun `should create a new account`() {
        // when
        val response = createAccount(
            aCreateAccount()
                .withOwner("Jan Kowalski")
                .withInitialBalance("2000.00"),
        )

        // then
        expectThat(response)
            .isOkResponse()
            .hasUUID()
    }

    @Test
    fun `should return BAD REQUEST when owner is missing`() {
        // when
        val response = createAccount(aCreateAccount().withOwner(""))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("owner", "Owner cannot be blank")
    }

    @Test
    fun `should return BAD REQUEST when owner is null`() {
        // when
        val response = createAccount(aCreateAccount().withOwner(null))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("owner", "Owner cannot be blank")
    }

    @Test
    fun `should return BAD REQUEST when initial balance is null`() {
        // when
        val response = createAccount(aCreateAccount().withInitialBalance(null))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("initialBalance", "Initial balance cannot be blank")
    }

    @Test
    fun `should return BAD REQUEST when initial balance is not a decimal number`() {
        // when
        val response = createAccount(aCreateAccount().withInitialBalance("1.999"))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("initialBalance", "Initial balance must be a valid decimal number")
        // TODO bardziej szczegolowo
    }
}
