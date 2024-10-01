package camilyed.github.io.currencyexchangeapi

import java.util.UUID

data class Account(
    val id: UUID,
    val owner: String,
    val balancePln: Double
)
