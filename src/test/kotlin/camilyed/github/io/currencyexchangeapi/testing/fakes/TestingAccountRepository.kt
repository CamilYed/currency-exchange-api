package camilyed.github.io.currencyexchangeapi.testing.fakes

import camilyed.github.io.currencyexchangeapi.domain.AccountRepository
import java.util.*

class TestingAccountRepository : AccountRepository {

    private var nextID: UUID? = null

    override fun nextAccountId(): UUID {
        return nextID ?: UUID.randomUUID()
    }

    fun setNextId(id: UUID) {
        this.nextID = id
    }
}
