package camilyed.github.io.currencyexchangeapi.domain

import camilyed.github.io.common.Money
import java.math.BigDecimal
import java.util.UUID

class Account private constructor(
    private val id: AccountId,
    private val owner: String,
    private var balancePln: Money = Money(BigDecimal.ZERO, "PLN"),
    private var balanceUsd: Money = Money(BigDecimal.ZERO, "USD"),
) {
    private val events = mutableListOf<AccountEvent>()

    init {
        require(balancePln.currency == "PLN") { "PLN balance must be in PLN" }
        require(balanceUsd.currency == "USD") { "USD balance must be in USD" }
    }

    fun exchangePlnToUsd(
        amountPln: Money,
        exchangeRate: ExchangeRate,
        operationId: OperationId,
    ) {
        require(!amountPln.isZero()) {
            throw InvalidAmountException("Amount must be greater than 0")
        }
        require(amountPln <= balancePln) {
            throw InsufficientFundsException(
                "Insufficient PLN balance",
            )
        }

        val amountUsd = Money(exchangeRate.convertFromPln(amountPln.amount), "USD")
        balancePln -= amountPln
        balanceUsd += amountUsd

        addEvent(
            AccountEvent.PlnToUsdExchangeEvent(
                accountId = id.value,
                operationId = operationId.value,
                amountPln = amountPln.amount,
                amountUsd = amountUsd.amount,
                exchangeRate = exchangeRate.rate,
            ),
        )
    }

    fun exchangeUsdToPln(amountUsd: Money, exchangeRate: ExchangeRate) {
        require(!amountUsd.isZero()) {
            throw InvalidAmountException("Amount must be greater than 0")
        }
        require(amountUsd <= balanceUsd) {
            throw InsufficientFundsException(
                "Insufficient USD balance",
            )
        }

        val amountPln = Money(exchangeRate.convertToPln(amountUsd.amount), "PLN")
        balanceUsd -= amountUsd
        balancePln += amountPln
    }

    fun getEvents(): List<AccountEvent> = events.toList()

    fun toSnapshot(): AccountSnapshot {
        return AccountSnapshot(
            id = id.value,
            owner = owner,
            balancePln = balancePln.amount,
            balanceUsd = balanceUsd.amount,
        )
    }

    private fun addEvent(event: AccountEvent) {
        events.add(event)
    }

    companion object {
        fun createNewAccount(data: CreateAccountData): Account {
            val account = Account(
                id = AccountId(data.id),
                owner = data.owner,
                balancePln = Money.pln(data.initialBalancePln),
                balanceUsd = Money.usd(BigDecimal.ZERO),
            )
            account.addEvent(
                AccountEvent.AccountCreatedEvent(
                    accountId = data.id,
                    operationId = data.operationId,
                    owner = data.owner,
                    initialBalancePln = data.initialBalancePln,
                ),
            )
            return account
        }

        fun fromSnapshot(snapshot: AccountSnapshot): Account {
            return Account(
                id = AccountId(snapshot.id),
                owner = snapshot.owner,
                balancePln = Money.pln(snapshot.balancePln),
                balanceUsd = Money.usd(snapshot.balanceUsd),
            )
        }
    }
}

data class AccountSnapshot(
    val id: UUID,
    val owner: String,
    val balancePln: BigDecimal,
    val balanceUsd: BigDecimal,
)
