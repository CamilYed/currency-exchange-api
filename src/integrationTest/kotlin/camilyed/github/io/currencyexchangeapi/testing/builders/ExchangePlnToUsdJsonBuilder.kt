package camilyed.github.io.currencyexchangeapi.testing.builders

import java.util.UUID

class ExchangePlnToUsdJsonBuilder {
    var accountId: String? = UUID.randomUUID().toString()
    var amount: String? = "100.00"
    var xRequestId: String? = UUID.randomUUID().toString()

    fun withAccountId(accountId: String) = apply {
        this.accountId = accountId
    }

    fun withAmount(amount: String) = apply {
        this.amount = amount
    }

    fun withXRequestId(xRequestId: String) = apply {
        this.xRequestId = xRequestId
    }

    fun withoutXRequestId() = apply {
        this.xRequestId = null
    }

    fun withoutAccountId() = apply {
        this.accountId = null
    }

    fun withoutAmount() = apply {
        this.amount = null
    }

    fun build(): Map<String, Any?> {
        return mapOf(
            "accountId" to accountId,
            "amount" to amount,
        )
    }

    companion object {
        fun anExchangePlnToUsd(): ExchangePlnToUsdJsonBuilder {
            return ExchangePlnToUsdJsonBuilder()
        }
    }
}
