package camilyed.github.io.currencyexchangeapi.domain

import java.math.BigDecimal
import java.math.RoundingMode

data class ExchangeRate(val rate: BigDecimal) {
    init {
        if (rate <= BigDecimal.ZERO) {
            throw InvalidExchangeRateException("Exchange rate must be greater than 0")
        }
    }

    fun convertFromPln(amountPln: BigDecimal): BigDecimal {
        return amountPln.divide(rate, 2, RoundingMode.HALF_EVEN)
    }

    fun convertToPln(amountUsd: BigDecimal): BigDecimal {
        return amountUsd.multiply(rate).setScale(2, RoundingMode.HALF_EVEN)
    }
}
