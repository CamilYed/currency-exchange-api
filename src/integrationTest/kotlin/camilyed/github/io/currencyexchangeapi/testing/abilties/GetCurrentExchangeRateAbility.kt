package camilyed.github.io.currencyexchangeapi.testing.abilties

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock

interface GetCurrentExchangeRateAbility {
    val wireMockServer: WireMockServer

    fun currentExchangeRateIs(rate: String) {
        wireMockServer.stubFor(
            WireMock.get("/api/exchangerates/rates/A/USD")
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                            {
                                "table": "A",
                                "currency": "USD",
                                "code": "USD",
                                "rates": [{
                                    "no": "091/A/NBP/2024",
                                    "effectiveDate": "2024-05-12",
                                    "mid": $rate
                                }]
                            }
                            """.trimIndent(),
                        ),
                ),
        )
    }
}
