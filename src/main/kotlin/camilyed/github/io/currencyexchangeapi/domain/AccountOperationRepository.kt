package camilyed.github.io.currencyexchangeapi.domain

import java.util.UUID

interface AccountOperationRepository {

    fun findAccountIdBy(operationId: UUID): UUID?

    fun save(events: List<AccountEvent>)
}
