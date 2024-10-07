package camilyed.github.io.currencyexchangeapi.testing.abilties

import com.github.tomakehurst.wiremock.client.WireMock

interface GetCurrentExchangeRateAbility {

    fun currentExchangeRateIs(rate: String) {
        WireMock.stubFor(
            WireMock.get(WireMock.urlEqualTo("/api/exchangerates/rates/A/USD"))
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
