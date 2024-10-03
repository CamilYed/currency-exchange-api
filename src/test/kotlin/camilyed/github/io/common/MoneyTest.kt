package camilyed.github.io.common

import org.junit.jupiter.api.Test
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure
import strikt.assertions.message
import java.math.BigDecimal

class MoneyTest {

    @Test
    fun `should create money with valid amount and currency`() {
        val money = Money(BigDecimal("100.00"), "USD")

        expectThat(money.amount).isEqualTo(BigDecimal("100.00"))
        expectThat(money.currency).isEqualTo("USD")
    }

    @Test
    fun `should throw exception when creating money with negative amount`() {
        expectCatching {
            Money(BigDecimal("-100.00"), "USD")
        }.isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Money amount must be greater than or equal to zero")
    }

    @Test
    fun `should throw exception when creating money with empty currency`() {
        expectCatching {
            Money(BigDecimal("100.00"), "")
        }.isFailure()
            .isA<IllegalArgumentException>()
            .message.isEqualTo("Currency cannot be empty")
    }

    @Test
    fun `should add two money objects with same currency`() {
        val money1 = Money(BigDecimal("100.00"), "USD")
        val money2 = Money(BigDecimal("50.00"), "USD")

        val result = money1 + money2

        expectThat(result.amount).isEqualTo(BigDecimal("150.00"))
        expectThat(result.currency).isEqualTo("USD")
    }

    @Test
    fun `should subtract two money objects with same currency`() {
        val money1 = Money(BigDecimal("100.00"), "USD")
        val money2 = Money(BigDecimal("50.00"), "USD")

        val result = money1 - money2

        expectThat(result.amount).isEqualTo(BigDecimal("50.00"))
        expectThat(result.currency).isEqualTo("USD")
    }
}