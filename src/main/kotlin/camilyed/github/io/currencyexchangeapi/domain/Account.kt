package camilyed.github.io.currencyexchangeapi.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class Account(
    private val id: UUID,
    private val owner: String,
    private var balancePln: BigDecimal,
    private var balanceUsd: BigDecimal = BigDecimal.ZERO
) {

    init {
        require(balancePln >= BigDecimal.ZERO) { "Initial balance cannot be negative" }
        balancePln = balancePln.setScale(2)
        balanceUsd = balanceUsd.setScale(2)
    }

    fun exchangePlnToUsd(amountPln: BigDecimal, exchangeRate: BigDecimal) {
        require(amountPln > BigDecimal.ZERO) { "Amount must be greater than 0" }
        val amountUsd = amountPln.divide(exchangeRate, 2, RoundingMode.HALF_EVEN)
        balancePln = balancePln.subtract(amountPln)
        balanceUsd = balanceUsd.add(amountUsd)
    }

    fun exchangeUsdToPln(amountUsd: BigDecimal, exchangeRate: BigDecimal) {
        require(amountUsd > BigDecimal.ZERO) { "Amount must be greater than 0" }
        val amountPln = amountUsd.multiply(exchangeRate).setScale(2, RoundingMode.HALF_EVEN)
        balanceUsd = balanceUsd.subtract(amountUsd)
        balancePln = balancePln.add(amountPln)
    }

    fun toSnapshot(): AccountSnapshot {
        return AccountSnapshot(
            id = id,
            owner = owner,
            balancePln = balancePln,
            balanceUsd = balanceUsd
        )
    }
}

data class AccountSnapshot(
    val id: UUID,
    val owner: String,
    val balancePln: BigDecimal,
    val balanceUsd: BigDecimal
)
