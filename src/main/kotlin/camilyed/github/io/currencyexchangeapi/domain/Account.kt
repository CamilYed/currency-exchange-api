package camilyed.github.io.currencyexchangeapi.domain

import camilyed.github.io.common.Money
import java.math.BigDecimal
import java.util.UUID

class Account(
    private val id: UUID,
    private val owner: String,
    private var balancePln: Money = Money(BigDecimal.ZERO, "PLN"),
    private var balanceUsd: Money = Money(BigDecimal.ZERO, "USD"),
) {
    init {
        require(balancePln.currency == "PLN") { "PLN balance must be in PLN" }
        require(balanceUsd.currency == "USD") { "USD balance must be in USD" }
    }

    fun exchangePlnToUsd(
        amountPln: Money,
        exchangeRate: ExchangeRate,
    ) {
        require(
            !amountPln.isZero(),
        ) { throw InvalidAmountException("Amount must be greater than 0") }
        require(
            amountPln <= balancePln,
        ) { throw InsufficientFundsException("Insufficient PLN balance") }

        val amountUsd = Money(exchangeRate.convertFromPln(amountPln.amount), "USD")
        balancePln -= amountPln
        balanceUsd += amountUsd
    }

    fun exchangeUsdToPln(
        amountUsd: Money,
        exchangeRate: ExchangeRate,
    ) {
        require(
            !amountUsd.isZero(),
        ) { throw InvalidAmountException("Amount must be greater than 0") }
        require(
            amountUsd <= balanceUsd,
        ) { throw InsufficientFundsException("Insufficient USD balance") }

        val amountPln = Money(exchangeRate.convertToPln(amountUsd.amount), "PLN")
        balanceUsd -= amountUsd
        balancePln += amountPln
    }

    fun toSnapshot(): AccountSnapshot {
        return AccountSnapshot(
            id = id,
            owner = owner,
            balancePln = balancePln.amount,
            balanceUsd = balanceUsd.amount,
        )
    }
}

data class AccountSnapshot(
    val id: UUID,
    val owner: String,
    val balancePln: BigDecimal,
    val balanceUsd: BigDecimal,
)
