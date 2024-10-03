package camilyed.github.io.currencyexchangeapi.domain

interface CurrentExchangeRateProvider {
    fun currentExchange(): ExchangeRate
}
