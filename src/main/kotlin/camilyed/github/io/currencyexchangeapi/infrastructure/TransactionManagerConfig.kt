@file:Suppress("UNCHECKED_CAST")

package camilyed.github.io.currencyexchangeapi.infrastructure

import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.Configuration

fun <T> executeInTransaction(block: () -> T): T {
    return inTransaction(block as () -> Any) as T
}

private var inTransaction: (() -> Any) -> Any = { block ->
    block()
}

@Configuration
class TransactionManagerConfig {

    @PostConstruct
    fun setupProductionTransaction() {
        inTransaction = { block ->
            transaction { block() }
        }
    }
}
