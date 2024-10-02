package camilyed.github.io.currencyexchangeapi.domain

import java.util.UUID

interface AccountRepository {

    fun nextAccountId(): UUID
}
