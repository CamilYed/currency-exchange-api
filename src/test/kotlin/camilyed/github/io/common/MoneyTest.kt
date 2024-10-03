package camilyed.github.io.common

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.math.BigDecimal

class MoneyTest {

    @Test
    fun `should create money with valid amount and currency`() {
        val money = Money(BigDecimal("100.00"), "USD")

        expectThat(money.amount).isEqualTo(BigDecimal("100.00"))
        expectThat(money.currency).isEqualTo("USD")
    }
}