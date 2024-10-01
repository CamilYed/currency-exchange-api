package camilyed.github.io.currencyexchangeapi

import java.math.BigDecimal
import java.util.UUID

data class Account(
    val id: UUID,
    val owner: String,
    val balancePln: BigDecimal
)
