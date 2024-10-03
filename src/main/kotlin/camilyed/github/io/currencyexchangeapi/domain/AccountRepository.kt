package camilyed.github.io.currencyexchangeapi.domain

import java.util.UUID

interface AccountRepository {

    fun nextAccountId(): UUID

    fun save(account: Account)

    fun find(id: UUID): Account?
}
