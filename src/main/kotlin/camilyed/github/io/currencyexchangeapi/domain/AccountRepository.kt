package camilyed.github.io.currencyexchangeapi.domain

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

interface AccountRepository {
    fun nextAccountId(): UUID = UUID.randomUUID()

    fun save(account: Account)

    fun find(id: UUID): Account?
}

@Component
@Profile("!test")
class InMemoryAccountRepository : AccountRepository {
    private val accounts = ConcurrentHashMap<UUID, AccountSnapshot>()

    override fun nextAccountId(): UUID = UUID.randomUUID()

    override fun save(account: Account) {
        val snapshot = account.toSnapshot()
        accounts[snapshot.id] = snapshot
    }

    override fun find(id: UUID): Account? {
        return accounts[id]?.let { Account.fromSnapshot(it) }
    }
}
