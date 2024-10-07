package camilyed.github.io.currencyexchangeapi.testing.utils

import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseCleaner {

    fun cleanAllTables() {
        transaction {
            val tables = TransactionManager.current().db.dialect.allTablesNames()
            tables.forEach { tableName ->
                exec("DELETE FROM $tableName")
            }
        }
    }
}
