package camilyed.github.io.currencyexchangeapi.adapters.nbp

import camilyed.github.io.currencyexchangeapi.domain.CurrentExchangeRateProvider
import camilyed.github.io.currencyexchangeapi.domain.ExchangeRate
import org.springframework.stereotype.Component

@Component
class NbpCurrentExchangeRateProvider(
    private val nbpFeignClient: NbpFeignClient,
) : CurrentExchangeRateProvider {
    override fun currentExchange(): ExchangeRate {
        val response = nbpFeignClient.getUsdToPlnRate()
        val rate = response.rates.firstOrNull()?.mid
        check(rate != null) { "No available exchange rates from NBP API" }
        return ExchangeRate(rate)
    }
}
