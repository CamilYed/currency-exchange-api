package camilyed.github.io.currencyexchangeapi.testing.fakes

import camilyed.github.io.currencyexchangeapi.domain.AccountEvent
import camilyed.github.io.currencyexchangeapi.domain.AccountOperationRepository
import camilyed.github.io.currencyexchangeapi.domain.OperationId
import java.util.UUID

class TestingAccountOperationRepository : AccountOperationRepository {
    override fun findAccountIdBy(operationId: OperationId): UUID? {
        return null
    }

    override fun save(events: List<AccountEvent>) {
        println("Save called with events: $events")
    }
}
