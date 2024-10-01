package camilyed.github.io.currencyexchangeapi

import java.util.*

class AccountService {

    fun create(command: CreateAccountCommand): Account {
        return Account(id = UUID.randomUUID(), owner = command.owner, balancePln = command.initialBalance)
    }
}
