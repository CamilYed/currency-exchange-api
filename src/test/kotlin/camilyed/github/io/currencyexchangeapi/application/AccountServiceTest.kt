package camilyed.github.io.currencyexchangeapi.application

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class AccountServiceTest {

    private val accountService = AccountService()

    @Test
    fun `should create a new account with valid details`() {
        // given
        val name = "Jan Kowalski"
        val initialBalance = BigDecimal(1000.0)

        // when
        val account = accountService.create(CreateAccountCommand(owner =  name, initialBalance = initialBalance))

        // then
        assertNotNull(account.id, "Account ID should not be null")
        assertEquals(initialBalance, account.balancePln, "Account balance in PLN should match initial balance")
        assertEquals(name, account.owner, "Account owner name should match provided name")
    }
}
