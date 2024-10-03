package camilyed.github.io.currencyexchangeapi.testing.fakes

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountRepository
import java.util.UUID

class TestingAccountRepository : AccountRepository {
    private val accounts = mutableMapOf<UUID, Account>()
    private var nextID: UUID? = null

    override fun nextAccountId(): UUID {
        return nextID ?: UUID.randomUUID()
    }

    override fun save(account: Account) {
        accounts[account.toSnapshot().id] = account
    }

    override fun find(id: UUID): Account? {
        return accounts[id]
    }

    fun setNextId(id: UUID) {
        this.nextID = id
    }
}
