package camilyed.github.io.common

import java.math.BigDecimal
import java.math.RoundingMode

data class Money(val amount: BigDecimal, val currency: String) {
    init {
        require(amount >= BigDecimal.ZERO) { "Money amount must be greater than or equal to zero" }
        require(currency == "PLN" || currency == "USD") {
            throw UnsupportedCurrencyException("Unsupported currency: $currency")
        }
        amount.setScale(2, RoundingMode.HALF_EVEN)
    }

    operator fun plus(other: Money): Money {
        require(currency == other.currency) { "Currencies must match to perform addition" }
        val result = amount.add(other.amount).setScale(2, RoundingMode.HALF_EVEN)
        return Money(result, currency)
    }

    operator fun minus(other: Money): Money {
        require(currency == other.currency) { "Currencies must match to perform subtraction" }
        require(amount >= other.amount) { "Insufficient funds" }
        val result = amount.subtract(other.amount).setScale(2, RoundingMode.HALF_EVEN)
        return Money(result, currency)
    }

    operator fun compareTo(other: Money): Int {
        require(currency == other.currency) { "Currencies must match to compare" }
        return amount.compareTo(other.amount)
    }

    fun isZero() = amount == BigDecimal.ZERO.setScale(2)

    companion object {
        fun pln(amount: BigDecimal) = Money(amount.setScale(2, RoundingMode.HALF_EVEN), "PLN")

        fun usd(amount: BigDecimal) = Money(amount.setScale(2, RoundingMode.HALF_EVEN), "USD")
    }
}

class UnsupportedCurrencyException(message: String) : RuntimeException(message)
