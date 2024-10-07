package camilyed.github.io.currencyexchangeapi.domain

import java.math.BigDecimal
import java.util.UUID

data class CreateAccountData(
    val id: UUID,
    val owner: String,
    val initialBalancePln: BigDecimal,
    val operationId: UUID,
)
