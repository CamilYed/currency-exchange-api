package camilyed.github.io.currencyexchangeapi.web

import camilyed.github.io.currencyexchangeapi.testing.BaseIntegrationTest
import camilyed.github.io.currencyexchangeapi.testing.abilties.CreateAccountAbility
import camilyed.github.io.currencyexchangeapi.testing.abilties.ExchangePlnToUsdAbility
import camilyed.github.io.currencyexchangeapi.testing.abilties.GetCurrentExchangeRateAbility
import camilyed.github.io.currencyexchangeapi.testing.assertion.hasPlnAmount
import camilyed.github.io.currencyexchangeapi.testing.assertion.hasProblemDetail
import camilyed.github.io.currencyexchangeapi.testing.assertion.hasUsdAmount
import camilyed.github.io.currencyexchangeapi.testing.assertion.isBadRequest
import camilyed.github.io.currencyexchangeapi.testing.assertion.isOkResponse
import camilyed.github.io.currencyexchangeapi.testing.assertion.isUnprocessableEntity
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountJsonBuilder.Companion.aCreateAccount
import camilyed.github.io.currencyexchangeapi.testing.builders.ExchangePlnToUsdJsonBuilder.Companion.anExchangePlnToUsd
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import java.util.UUID

class ExchangePlnToUsdIntegrationTest :
    BaseIntegrationTest(),
    CreateAccountAbility,
    ExchangePlnToUsdAbility,
    GetCurrentExchangeRateAbility {

    @BeforeEach
    fun setupSpec() {
        currentExchangeRateIs("4.0")
    }

    @Test
    fun `should exchange PLN to USD successfully`() {
        // given
        val accountId = thereIsAnAccount(aCreateAccount().withInitialBalance("1000.00"))

        // and
        currentExchangeRateIs("4.0")

        // when
        val response = exchangePlnToUsd(
            anExchangePlnToUsd()
                .withAccountId(accountId)
                .withAmount("400"),
        )

        // then
        expectThat(response)
            .isOkResponse()
            .hasPlnAmount("600.00")
            .hasUsdAmount("100.00")
    }

    @Test
    fun `should return error when accountId is missing`() {
        // when
        val response = exchangePlnToUsd(anExchangePlnToUsd().withoutAccountId())

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("accountId", "Account ID cannot be blank")
    }

    @Test
    fun `should return error when amount is missing`() {
        // when
        val response = exchangePlnToUsd(anExchangePlnToUsd().withoutAmount())

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("amount", "Amount cannot be blank")
    }

    @Test
    fun `should return error when amount is not a valid number`() {
        // when
        val response = exchangePlnToUsd(anExchangePlnToUsd().withAmount("invalid"))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("amount", "Amount must be a valid decimal number")
    }

    @Test
    fun `should return error when X-Request-Id header is missing`() {
        // when
        val response = exchangePlnToUsd(anExchangePlnToUsd().withoutXRequestId())

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("X-Request-Id", "X-Request-Id is required and must be a valid UUID")
    }

    @Test
    fun `should return error when X-Request-Id header is not valid UUID`() {
        // when
        val response = exchangePlnToUsd(anExchangePlnToUsd().withXRequestId("not uuid"))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("X-Request-Id", "X-Request-Id is required and must be a valid UUID")
    }

    @Test
    fun `should return error when amount is zero`() {
        // given
        val accountId = thereIsAnAccount(aCreateAccount())

        // when
        val response = exchangePlnToUsd(
            anExchangePlnToUsd()
                .withAccountId(accountId)
                .withAmount("0"),
        )

        // then
        expectThat(response)
            .isUnprocessableEntity()
            .hasProblemDetail("amount", "Amount must be greater than 0")
    }

    @Test
    fun `should return error when not enough PLN funds for exchange`() {
        // given
        val accountId = thereIsAnAccount(aCreateAccount().withInitialBalance("100.00"))

        // and
        currentExchangeRateIs("4.0")

        // when
        val response = exchangePlnToUsd(
            anExchangePlnToUsd()
                .withAccountId(accountId)
                .withAmount("200"),
        )

        // then
        expectThat(response)
            .isUnprocessableEntity()
            .hasProblemDetail("balance", "Insufficient PLN balance")
    }

    @Test
    fun `should return the same result when the same X-Request-Id is used multiple times`() {
        // given
        val accountId = thereIsAnAccount(aCreateAccount().withInitialBalance("1000.00"))

        // and
        currentExchangeRateIs("4.0")

        val requestId = UUID.randomUUID().toString()
        val firstResponse = exchangePlnToUsd(
            anExchangePlnToUsd()
                .withAccountId(accountId)
                .withAmount("400")
                .withXRequestId(requestId),
        )

        expectThat(firstResponse)
            .isOkResponse()
            .hasPlnAmount("600.00")
            .hasUsdAmount("100.00")

        val secondResponse = exchangePlnToUsd(
            anExchangePlnToUsd()
                .withAccountId(accountId)
                .withAmount("400")
                .withXRequestId(requestId),
        )

        expectThat(secondResponse)
            .isOkResponse()
            .hasPlnAmount("600.00")
            .hasUsdAmount("100.00")
    }
}
