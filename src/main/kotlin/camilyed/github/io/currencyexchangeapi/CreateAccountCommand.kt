package camilyed.github.io.currencyexchangeapi

import java.math.BigDecimal

data class CreateAccountCommand(
    val owner: String,
    val initialBalance: BigDecimal
)
