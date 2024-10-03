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

    fun exchangePlnToUsd(amountPln: BigDecimal, exchangeRate: ExchangeRate) {
        require(amountPln > BigDecimal.ZERO) { "Amount must be greater than 0" }
        require(amountPln <= balancePln) { "Insufficient PLN balance" }
        val amountUsd = exchangeRate.convertFromPln(amountPln)
        balancePln = balancePln.subtract(amountPln)
        balanceUsd = balanceUsd.add(amountUsd)
    }

    fun exchangeUsdToPln(amountUsd: BigDecimal, exchangeRate: ExchangeRate) {
        require(amountUsd > BigDecimal.ZERO) { "Amount must be greater than 0" }
        require(amountUsd <= balanceUsd) { "Insufficient USD balance" }
        val amountPln = exchangeRate.convertToPln(amountUsd)
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
