package camilyed.github.io.currencyexchangeapi.testing.builders

import camilyed.github.io.currencyexchangeapi.application.ExchangeUsdToPlnCommand
import java.math.BigDecimal
import java.util.UUID

class ExchangeUsdToPlnCommandBuilder private constructor() {
    private var accountId: UUID = UUID.randomUUID()
    private var amount: BigDecimal = BigDecimal("100.00")

    fun withAccountId(accountId: UUID) = apply { this.accountId = accountId }

    fun withAmount(amount: String) = apply { this.amount = BigDecimal(amount) }

    fun build(): ExchangeUsdToPlnCommand {
        return ExchangeUsdToPlnCommand(
            accountId = accountId,
            amount = amount,
        )
    }

    companion object {
        fun anExchangeToPln(): ExchangeUsdToPlnCommandBuilder {
            return ExchangeUsdToPlnCommandBuilder()
        }
    }
}
