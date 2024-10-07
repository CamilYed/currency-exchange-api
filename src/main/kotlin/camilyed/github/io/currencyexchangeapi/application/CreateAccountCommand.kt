package camilyed.github.io.currencyexchangeapi.application

import java.math.BigDecimal
import java.util.UUID

data class CreateAccountCommand(
    val owner: String,
    val initialBalance: BigDecimal,
    val commandId: UUID,
)
