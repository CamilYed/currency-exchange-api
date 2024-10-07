package camilyed.github.io.currencyexchangeapi.domain

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

interface AccountOperationRepository {

    fun findAccountIdBy(operationId: UUID): UUID?

    fun save(events: List<AccountEvent>)
}

@Component
@Profile("!test")
class InMemoryAccountOperationRepository : AccountOperationRepository {

    private val operations = ConcurrentHashMap<UUID, UUID>()
    private val events = mutableListOf<AccountEvent>()

    override fun findAccountIdBy(operationId: UUID): UUID? {
        return operations[operationId]
    }

    override fun save(events: List<AccountEvent>) {
        events.forEach { event ->
            this.events.add(event)
            operations[event.operationId] = event.accountId
        }
    }
}
