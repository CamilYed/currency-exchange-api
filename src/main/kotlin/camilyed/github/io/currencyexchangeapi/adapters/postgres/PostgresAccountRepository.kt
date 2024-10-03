package camilyed.github.io.currencyexchangeapi.adapters.postgres

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountRepository
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.upsert
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PostgresAccountRepository : AccountRepository {

    override fun save(account: Account) {
        val snapshot = account.toSnapshot()
        AccountsTable.upsert {
            it[id] = snapshot.id
            it[owner] = snapshot.owner
            it[balancePln] = snapshot.balancePln
            it[balanceUsd] = snapshot.balanceUsd
        }
    }

    override fun find(id: UUID): Account? {
        return AccountsTable.select { AccountsTable.id eq id }
            .mapNotNull { it.toAccount() }
            .singleOrNull()
    }
}
