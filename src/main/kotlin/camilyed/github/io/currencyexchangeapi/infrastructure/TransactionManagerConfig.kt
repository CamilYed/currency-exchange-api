@file:Suppress("UNCHECKED_CAST")

package camilyed.github.io.currencyexchangeapi.infrastructure

import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.Configuration

fun <T> inTransaction(block: () -> T): T {
    return executeInTransaction(block as () -> Any) as T
}

private var executeInTransaction: (() -> Any) -> Any = { block ->
    block()
}

@Configuration
class TransactionManagerConfig {

    @PostConstruct
    fun setupProductionTransaction() {
        executeInTransaction = { block ->
            transaction { block() }
        }
    }
}
