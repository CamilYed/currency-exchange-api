package camilyed.github.io.currencyexchangeapi.adapters.postgres

import camilyed.github.io.currencyexchangeapi.domain.AccountEvent
import camilyed.github.io.currencyexchangeapi.domain.AccountOperationRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

@Component
class PostgresAccountOperationRepository(
    private val clock: Clock,
) : AccountOperationRepository {

    override fun findAccountIdBy(operationId: UUID): UUID? {
        return transaction {
            AccountOperationsTable
                .select(AccountOperationsTable.accountId)
                .where { AccountOperationsTable.operationId eq operationId }
                .map { it[AccountOperationsTable.accountId] }
                .singleOrNull()
        }
    }

    override fun save(events: List<AccountEvent>) {
        events.forEach { event ->
            val timestamp = LocalDateTime.now(clock)
            when (event) {
                is AccountEvent.AccountCreatedEvent -> {
                    AccountOperationsTable.insert {
                        it[id] = UUID.randomUUID()
                        it[accountId] = event.accountId
                        it[operationType] = "CREATE_ACCOUNT"
                        it[operationId] = event.operationId
                        it[createdAt] = timestamp
                        it[amountPln] = event.initialBalancePln
                        it[amountUsd] = null
                        it[exchangeRate] = null
                        it[description] = description(event)
                    }
                }
            }
        }
    }

    private fun description(event: AccountEvent.AccountCreatedEvent) =
        "Created account for ${event.owner} with initial balance of ${event.initialBalancePln} PLN"
}
