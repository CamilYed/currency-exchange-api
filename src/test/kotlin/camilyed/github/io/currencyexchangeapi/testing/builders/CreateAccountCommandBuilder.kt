package camilyed.github.io.currencyexchangeapi.testing.builders

import camilyed.github.io.currencyexchangeapi.application.CreateAccountCommand
import java.math.BigDecimal

class CreateAccountCommandBuilder private constructor() {
    private var owner: String = "Grzegorz Brzęczyszczykiewicz"
    private var initialBalance: BigDecimal = BigDecimal("100.00")

    fun withOwner(owner: String) = apply { this.owner = owner }
    fun withInitialBalance(initialBalance: String) = apply { this.initialBalance = BigDecimal(initialBalance) }

    fun build(): CreateAccountCommand {
        return CreateAccountCommand(
            owner = owner,
            initialBalance = initialBalance
        )
    }

    companion object {
        fun aCreateAccountCommand(): CreateAccountCommandBuilder {
            return CreateAccountCommandBuilder()
        }
    }
}
