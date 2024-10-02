package camilyed.github.io.currencyexchangeapi.testing.assertions

import camilyed.github.io.currencyexchangeapi.domain.Account
import strikt.api.Assertion
import java.math.BigDecimal

fun Assertion.Builder<Account>.hasOwner(expectedOwner: String) =
    assert("has owner $expectedOwner") {
        if (it.owner == expectedOwner) pass() else fail()
    }

fun Assertion.Builder<Account>.hasInitialBalance(expectedBalance: Double) =
    assert("has initial balance $expectedBalance") {
        if (it.balancePln == BigDecimal(expectedBalance)) pass() else fail(
            description = "in fact it is %s",
            actual = it.balancePln
        )
    }
