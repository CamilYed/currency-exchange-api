package camilyed.github.io.currencyexchangeapi.testing.fakes

import camilyed.github.io.currencyexchangeapi.domain.CurrentExchangeRateProvider
import camilyed.github.io.currencyexchangeapi.domain.ExchangeRate
import java.math.BigDecimal

class TestingCurrentExchangeRateProvider : CurrentExchangeRateProvider {
    private var currentExchange: ExchangeRate = ExchangeRate(BigDecimal.valueOf(4.00))

    override fun currentExchange() = currentExchange

    fun setCurrentExchange(exchangeRate: ExchangeRate) {
        this.currentExchange = exchangeRate
    }
}
