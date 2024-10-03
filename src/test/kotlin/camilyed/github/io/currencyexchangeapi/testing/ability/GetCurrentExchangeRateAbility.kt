package camilyed.github.io.currencyexchangeapi.testing.ability

import camilyed.github.io.currencyexchangeapi.domain.ExchangeRate
import camilyed.github.io.currencyexchangeapi.testing.fakes.TestingCurrentExchangeRateProvider
import java.math.BigDecimal

interface GetCurrentExchangeRateAbility {
    val exchangeRateProvider: TestingCurrentExchangeRateProvider

    fun currentExchangeRateIs(rate: String) {
        exchangeRateProvider.setCurrentExchange(ExchangeRate(BigDecimal(rate)))
    }
}
