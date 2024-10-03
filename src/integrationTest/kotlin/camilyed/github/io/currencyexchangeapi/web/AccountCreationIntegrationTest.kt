package camilyed.github.io.currencyexchangeapi.web

import camilyed.github.io.currencyexchangeapi.testing.BaseIntegrationTest
import camilyed.github.io.currencyexchangeapi.testing.abilties.CreateAccountAbility
import camilyed.github.io.currencyexchangeapi.testing.abilties.GetCurrentExchangeRateAbility
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountJsonBuilder.Companion.aCreateAccount
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class AccountCreationIntegrationTest :
    BaseIntegrationTest(),
    CreateAccountAbility,
    GetCurrentExchangeRateAbility {

    @Test
    fun `should create a new account`() {
        // given
        val builder =
            aCreateAccount()
                .withOwner("Jan Kowalski")
                .withInitialBalance("2000.00")

        // when
        val response = createAccount(builder)

        // then
        expectThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
    }
}
