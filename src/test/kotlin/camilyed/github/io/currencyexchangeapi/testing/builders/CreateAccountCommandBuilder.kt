package camilyed.github.io.currencyexchangeapi.testing.builders

import camilyed.github.io.currencyexchangeapi.application.CreateAccountCommand
import camilyed.github.io.currencyexchangeapi.domain.OperationId
import java.math.BigDecimal
import java.util.UUID

class CreateAccountCommandBuilder private constructor() {
    private var owner: String = "Grzegorz Brzęczyszczykiewicz"
    private var initialBalance: BigDecimal = BigDecimal("100.00")

    fun withOwner(owner: String) = apply { this.owner = owner }

    fun withInitialBalance(initialBalance: String) =
        apply {
            this.initialBalance = BigDecimal(initialBalance)
        }

    fun build(): CreateAccountCommand {
        return CreateAccountCommand(
            owner = owner,
            initialBalance = initialBalance,
            OperationId(UUID.randomUUID()),
        )
    }

    companion object {
        fun aCreateAccountCommand(): CreateAccountCommandBuilder {
            return CreateAccountCommandBuilder()
        }
    }
}
