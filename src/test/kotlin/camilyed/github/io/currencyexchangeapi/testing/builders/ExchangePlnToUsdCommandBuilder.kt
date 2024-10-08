package camilyed.github.io.currencyexchangeapi.testing.builders

import camilyed.github.io.currencyexchangeapi.application.ExchangePlnToUsdCommand
import java.math.BigDecimal
import java.util.UUID

class ExchangePlnToUsdCommandBuilder private constructor() {
    private var accountId: UUID = UUID.randomUUID()
    private var amount: BigDecimal = BigDecimal("100.00")

    fun withAccountId(accountId: UUID) = apply { this.accountId = accountId }

    fun withAmount(amount: String) = apply { this.amount = BigDecimal(amount) }

    fun build(): ExchangePlnToUsdCommand {
        return ExchangePlnToUsdCommand(
            accountId = accountId,
            amount = amount,
            commandId = UUID.randomUUID(),
        )
    }

    companion object {
        fun anExchangeToUsd(): ExchangePlnToUsdCommandBuilder {
            return ExchangePlnToUsdCommandBuilder()
        }
    }
}
