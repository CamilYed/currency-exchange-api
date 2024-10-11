package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.currencyexchangeapi.domain.OperationId
import java.math.BigDecimal
import java.util.UUID

data class ExchangePlnToUsdCommand(
    val accountId: UUID,
    val amount: BigDecimal,
    val operationId: OperationId,
)
