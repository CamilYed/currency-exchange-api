package camilyed.github.io.currencyexchangeapi.testing.abilties

import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountJsonBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity

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
}
