package camilyed.github.io.currencyexchangeapi.config

import camilyed.github.io.currencyexchangeapi.application.inTransaction
import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.Configuration

@Configuration
class TransactionManagerConfig {
    @PostConstruct
    fun setupProductionTransaction() {
        inTransaction = { block ->
            transaction { block() } // Using Exposed or any transaction manager
        }
    }
}
