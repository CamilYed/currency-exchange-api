package camilyed.github.io.currencyexchangeapi

data class CreateAccountCommand(
    val owner: String,
    val initialBalance: Double
)
