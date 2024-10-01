package camilyed.github.io.currencyexchangeapi.application

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class AccountServiceTest {

    private val accountService = AccountService()

    @Test
    fun `should create a new account with valid details`() {
        // given
        val name = "Jan Kowalski"
        val initialBalance = BigDecimal(1000.0)

        // when
        val account = accountService.create(CreateAccountCommand(owner = name, initialBalance = initialBalance))

        // then
        assertNotNull(account.id, "Account ID should not be null")
        assertEquals(initialBalance, account.balancePln, "Account balance in PLN should match initial balance")
        assertEquals(name, account.owner, "Account owner name should match provided name")
    }

    @Test
    fun `should throw exception when trying to create account with negative balance`() {
        // given
        val name = "Jan Kowalski"
        val initialBalance = BigDecimal(1000.0)

        // when
        val exception = assertThrows<IllegalArgumentException> {
            accountService.create(CreateAccountCommand(owner = name, initialBalance = initialBalance))
        }

        // and
        assertEquals("Initial balance cannot be negative", exception.message)
    }
}