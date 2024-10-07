package camilyed.github.io.currencyexchangeapi.testing.abilties

import camilyed.github.io.currencyexchangeapi.api.AccountEndpoint
import camilyed.github.io.currencyexchangeapi.testing.assertion.isOkResponse
import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountJsonBuilder
import camilyed.github.io.currencyexchangeapi.testing.utils.parseBodyToType
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import strikt.api.expectThat

interface CreateAccountAbility : MakeRequestAbility {
    fun createAccount(builder: CreateAccountJsonBuilder): ResponseEntity<String> {
        val accountJson = builder.build()
        val httpHeaders = HttpHeaders()
        httpHeaders["X-Request-Id"] = builder.xRequestId
        return post(
            url = "/api/accounts",
            body = accountJson,
            headers = httpHeaders,
            responseType = String::class.java,
        )
    }

    fun thereIsAnAccount(builder: CreateAccountJsonBuilder): String {
        val response = createAccount(builder)
        expectThat(response).isOkResponse()
        return parseBodyToType<AccountEndpoint.AccountCreatedJson>(response).id
    }
}
