package camilyed.github.io.currencyexchangeapi.application

import java.math.BigDecimal
import java.util.UUID

data class ExchangeUsdToPlnCommand(
    val accountId: UUID,
    val amount: BigDecimal,
    val exchangeRate: BigDecimal,
)
