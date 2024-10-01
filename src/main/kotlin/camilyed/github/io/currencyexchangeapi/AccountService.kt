package camilyed.github.io.currencyexchangeapi

import java.util.*

class AccountService {

    fun create(name: String, balance: Double): Account {
        return Account(id = UUID.randomUUID(), owner = name, balancePln = balance)
    }
}
