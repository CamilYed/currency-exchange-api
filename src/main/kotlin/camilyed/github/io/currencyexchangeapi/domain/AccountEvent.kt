package camilyed.github.io.currencyexchangeapi.domain

import java.math.BigDecimal
import java.util.UUID

sealed class AccountEvent(
    open val accountId: UUID,
    open val operationId: OperationId,
) {
    data class AccountCreatedEvent(
        override val accountId: UUID,
        override val operationId: OperationId,
        val owner: String,
        val initialBalancePln: BigDecimal,
    ) : AccountEvent(accountId, operationId)

    data class PlnToUsdExchangeEvent(
        override val accountId: UUID,
        override val operationId: OperationId,
        val amountPln: BigDecimal,
        val amountUsd: BigDecimal,
        val exchangeRate: BigDecimal,
    ) : AccountEvent(accountId, operationId)
}
