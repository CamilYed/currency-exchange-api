package camilyed.github.io.currencyexchangeapi.testing.builders

import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import java.math.BigDecimal
import java.util.*

class AccountSnapshotBuilder private constructor() {
    private var id: UUID = UUID.randomUUID()
    private var owner: String = "Jan Kowalski"
    private var balancePln: BigDecimal = BigDecimal("1000.00")
    private var balanceUsd: BigDecimal = BigDecimal.ZERO

    fun withId(id: UUID) = apply { this.id = id }
    fun withOwner(owner: String) = apply { this.owner = owner }
    fun withBalancePln(balance: String) = apply { this.balancePln = BigDecimal(balance) }
    fun withBalanceUsd(balance: String) = apply { this.balanceUsd = BigDecimal(balance) }

    fun build(): AccountSnapshot = AccountSnapshot(
        id = id,
        owner = owner,
        balancePln = balancePln,
        balanceUsd = balanceUsd
    )

    companion object {
        fun anAccount(): AccountSnapshotBuilder {
            return AccountSnapshotBuilder()
        }
    }
}
