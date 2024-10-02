package camilyed.github.io.currencyexchangeapi.application

import java.math.BigDecimal
import java.util.UUID

data class ExchangePlnToUsdCommand(
    val accountId: UUID,
    val amount: BigDecimal,
    val exchangeRate: BigDecimal
)
