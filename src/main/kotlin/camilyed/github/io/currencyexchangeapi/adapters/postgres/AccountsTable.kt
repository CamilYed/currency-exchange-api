package camilyed.github.io.currencyexchangeapi.adapters.postgres

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow

object AccountsTable : UUIDTable("accounts") {
    val owner = varchar("owner", OWNER_MAX_CHAR)
    val balancePln = decimal("balance_pln", precision = PRECISION, scale = SCALE)
    val balanceUsd = decimal("balance_usd", precision = PRECISION, scale = SCALE)
}

fun ResultRow.toAccount(): Account {
    return Account.fromSnapshot(
        AccountSnapshot(
            id = this[AccountsTable.id].value,
            owner = this[AccountsTable.owner],
            balancePln = this[AccountsTable.balancePln],
            balanceUsd = this[AccountsTable.balanceUsd],
        ),
    )
}

private const val OWNER_MAX_CHAR = 255
private const val PRECISION = 20
private const val SCALE = 2
