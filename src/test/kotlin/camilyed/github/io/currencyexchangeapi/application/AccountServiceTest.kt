package camilyed.github.io.currencyexchangeapi.application

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.testing.CreateAccountCommandBuilder
import camilyed.github.io.currencyexchangeapi.testing.CreateAccountCommandBuilder.Companion.anAccount
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class AccountServiceTest {

    private val accountService = AccountService()

    @Test
    fun `should create a new account with valid details`() {
        // given
        val command = anAccount()
            .withOwner("Jan Kowalski")
            .withInitialBalance(BigDecimal(1000.0))

        // when
        val account = createAccount(command)

        // then
        assertNotNull(account.id, "Account ID should not be null")
        assertEquals(BigDecimal(1000.0), account.balancePln, "Account balance in PLN should match initial balance")
        assertEquals("Jan Kowalski", account.owner, "Account owner name should match provided name")
    }

    @Test
    fun `should throw exception when trying to create account with negative balance`() {
        // given
        val command = anAccount()
            .withInitialBalance(BigDecimal(-1000.0))

        // when
        val exception = assertThrows<IllegalArgumentException> {
            createAccount(command)
        }

        // and
        assertEquals("Initial balance cannot be negative", exception.message)
    }

    private fun createAccount(command: CreateAccountCommandBuilder): Account {
        return accountService.create(command.build())
    }
}
