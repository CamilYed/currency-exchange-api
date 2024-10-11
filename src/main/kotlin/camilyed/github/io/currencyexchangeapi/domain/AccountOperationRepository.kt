package camilyed.github.io.currencyexchangeapi.domain

import java.util.UUID

interface AccountOperationRepository {

    fun findAccountIdBy(operationId: OperationId): UUID?

    fun save(events: List<AccountEvent>)
}
