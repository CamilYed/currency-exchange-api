package camilyed.github.io.currencyexchangeapi.testing.assertions

import camilyed.github.io.currencyexchangeapi.domain.Account
import strikt.api.Assertion
import java.math.BigDecimal
import java.util.*

fun Assertion.Builder<Account>.hasId(expectedId: String) =
    assert("has id $expectedId") {
        if (it.id == UUID.fromString(expectedId)) pass() else fail(
            description = "in fact it is %s",
            actual = it.id
        )
    }

fun Assertion.Builder<Account>.hasOwner(expectedOwner: String) =
    assert("has owner $expectedOwner") {
        if (it.owner == expectedOwner) pass() else fail()
    }

fun Assertion.Builder<Account>.hasBalanceInPln(expectedBalance: Double) =
    assert("has initial balance $expectedBalance") {
        if (it.balancePln == BigDecimal(expectedBalance)) pass() else fail(
            description = "in fact it is %s",
            actual = it.balancePln
        )
    }

fun Assertion.Builder<Account>.hasBalanceInUsd(expectedBalance: Double) =
    assert("has balance in usd $expectedBalance") {
        if (it.balanceUsd == BigDecimal(expectedBalance)) pass() else fail(
            description = "in fact it is %s",
            actual = it.balancePln
        )
    }
