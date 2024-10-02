package camilyed.github.io.currencyexchangeapi.testing.builders

import camilyed.github.io.currencyexchangeapi.application.ExchangeUsdToPlnCommand
import java.math.BigDecimal
import java.util.*

class ExchangeUsdToPlnCommandBuilder private constructor() {
    private var accountId: UUID = UUID.randomUUID()
    private var amount: BigDecimal = BigDecimal("100.00")
    private var exchangeRate: BigDecimal = BigDecimal("4.0")

    fun withAccountId(accountId: UUID) = apply { this.accountId = accountId }
    fun withAmount(amount: String) = apply { this.amount = BigDecimal(amount) }
    fun withExchangeRate(exchangeRate: String) = apply { this.exchangeRate = BigDecimal(exchangeRate) }

    fun build(): ExchangeUsdToPlnCommand {
        return ExchangeUsdToPlnCommand(
            accountId = accountId,
            amount = amount,
            exchangeRate = exchangeRate
        )
    }

    companion object {
        fun anExchangeToPln(): ExchangeUsdToPlnCommandBuilder {
            return ExchangeUsdToPlnCommandBuilder()
        }
    }
}
