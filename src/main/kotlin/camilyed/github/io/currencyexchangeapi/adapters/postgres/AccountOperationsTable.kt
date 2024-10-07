package camilyed.github.io.currencyexchangeapi.adapters.postgres

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object AccountOperationsTable : Table("account_operations") {
    private const val OPERATION_TYPE_MAX_LENGTH = 255
    private const val PRECISION = 20
    private const val SCALE_PLN = 2
    private const val SCALE_EXCHANGE_RATE = 4

    val id = uuid("id")
    val accountId = uuid("account_id").references(AccountsTable.id)
    val operationType = varchar("operation_type", OPERATION_TYPE_MAX_LENGTH)
    val operationId = uuid("operation_id")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val amountPln = decimal("amount_pln", PRECISION, SCALE_PLN).nullable()
    val amountUsd = decimal("amount_usd", PRECISION, SCALE_PLN).nullable()
    val exchangeRate = decimal("exchange_rate", PRECISION, SCALE_EXCHANGE_RATE).nullable()
    val description = text("description").nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_AccountOperations_Id")
}
