package camilyed.github.io.currencyexchangeapi.testing.abilties

import camilyed.github.io.currencyexchangeapi.testing.builders.ExchangePlnToUsdJsonBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity

interface ExchangePlnToUsdAbility : MakeRequestAbility {
    fun exchangePlnToUsd(builder: ExchangePlnToUsdJsonBuilder): ResponseEntity<String> {
        val exchangeJson = builder.build()
        val httpHeaders = HttpHeaders()
        httpHeaders["X-Request-Id"] = builder.xRequestId
        return put(
            url = "/api/accounts//exchange-pln-to-usd",
            body = exchangeJson,
            headers = httpHeaders,
            responseType = String::class.java,
        )
    }
}
