package camilyed.github.io.currencyexchangeapi.web

import camilyed.github.io.currencyexchangeapi.testing.BaseIntegrationTest
import camilyed.github.io.currencyexchangeapi.testing.abilties.CreateAccountAbility
import camilyed.github.io.currencyexchangeapi.testing.assertion.hasProblemDetail
import camilyed.github.io.currencyexchangeapi.testing.assertion.hasUUID
import camilyed.github.io.currencyexchangeapi.testing.assertion.isBadRequest
import camilyed.github.io.currencyexchangeapi.testing.assertion.isOkResponse
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountJsonBuilder.Companion.aCreateAccount
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class AccountCreationIntegrationTest :
    BaseIntegrationTest(),
    CreateAccountAbility {

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
    fun `should get errror when owner is missing`() {
        // when
        val response = createAccount(aCreateAccount().withOwner(""))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("owner", "Owner cannot be blank")
    }

    @Test
    fun `should get error when owner is null`() {
        // when
        val response = createAccount(aCreateAccount().withOwner(null))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("owner", "Owner cannot be blank")
    }

    @Test
    fun `should get error when initial balance is null`() {
        // when
        val response = createAccount(aCreateAccount().withInitialBalance(null))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("initialBalance", "Initial balance cannot be blank")
    }

    @Test
    fun `should get error when initial balance is not a decimal number`() {
        // when
        val response = createAccount(aCreateAccount().withInitialBalance("1.999"))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("initialBalance", "Initial balance must be a valid decimal number")
        // TODO more detailed response
    }

    @Test
    fun `should create account only once for the same request id`() {
        // given
        val requestId = "05df4b53-1d60-420b-9b46-63c867c0dd02"

        // when
        val firstResponse = createAccount(aCreateAccount().withXRequestId(requestId))
        val secondResponse = createAccount(aCreateAccount().withXRequestId(requestId))

        // then
        expectThat(firstResponse).isOkResponse().hasUUID()
        expectThat(secondResponse).isOkResponse().hasUUID()

        // and
        expectThat(firstResponse.body).isEqualTo(secondResponse.body)
    }

    @Test
    fun `should get error if X-Request-Id is missing`() {
        // when
        val response = createAccount(aCreateAccount().withoutXRequestId())

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("X-Request-Id", "X-Request-Id is required and must be a valid UUID")
    }

    @Test
    fun `should get error if X-Request-Id is not UUID`() {
        // when
        val response = createAccount(aCreateAccount().withXRequestId("not uuid"))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail("X-Request-Id", "X-Request-Id is required and must be a valid UUID")
    }

    @Test
    fun `should get error if X-Request-Id is insecure`() {
        // given
        val insecureUUID = "00000000-0000-0000-0000-000000000000"

        // when
        val response = createAccount(aCreateAccount().withXRequestId(insecureUUID))

        // then
        expectThat(response)
            .isBadRequest()
            .hasProblemDetail(
                "X-Request-Id",
                "Insecure operation: UUID $insecureUUID does not meet security requirements.",
            )
    }

    // TODO add test for optimistick locking ?
}
