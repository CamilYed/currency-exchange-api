package camilyed.github.io.common

import java.math.BigDecimal

data class Money(val amount: BigDecimal, val currency: String) {

    init {
        require(amount >= BigDecimal.ZERO) { "Money amount must be greater than or equal to zero" }
        require(currency.isNotEmpty()) { "Currency cannot be empty" }
    }

    operator fun plus(other: Money): Money {
        require(currency == other.currency) { "Currencies must match to perform addition" }
        return Money(amount.add(other.amount), currency)
    }
}