package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.currencyexchangeapi.domain.OperationId
import java.math.BigDecimal

data class CreateAccountCommand(
    val owner: String,
    val initialBalance: BigDecimal,
    val operationId: OperationId,
)
