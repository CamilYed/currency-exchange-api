package camilyed.github.io.currencyexchangeapi.testing.assertions

import camilyed.github.io.currencyexchangeapi.domain.Account
import camilyed.github.io.currencyexchangeapi.domain.AccountSnapshot
import strikt.api.Assertion
import java.math.BigDecimal
import java.util.*

fun Assertion.Builder<AccountSnapshot>.hasId(expectedId: String) =
    assert("has id $expectedId") {
        if (it.id == UUID.fromString(expectedId)) pass() else fail(
            description = "in fact it is %s",
            actual = it.id
        )
    }

fun Assertion.Builder<AccountSnapshot>.hasOwner(expectedOwner: String) =
    assert("has owner $expectedOwner") {
        if (it.owner == expectedOwner) pass() else fail()
    }

fun Assertion.Builder<AccountSnapshot>.hasBalanceInPln(expectedBalance: Double) =
    assert("has initial balance $expectedBalance") {
        if (it.balancePln == BigDecimal(expectedBalance)) pass() else fail(
            description = "in fact it is %s",
            actual = it.balancePln
        )
    }

fun Assertion.Builder<AccountSnapshot>.hasBalanceInUsd(expectedBalance: Double) =
    assert("has balance in usd $expectedBalance") {
        if (it.balanceUsd == BigDecimal(expectedBalance)) pass() else fail(
            description = "in fact it is %s",
            actual = it.balancePln
        )
    }
