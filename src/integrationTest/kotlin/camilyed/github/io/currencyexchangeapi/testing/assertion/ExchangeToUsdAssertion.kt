package camilyed.github.io.currencyexchangeapi.testing.assertion

import camilyed.github.io.currencyexchangeapi.testing.utils.parseBodyToType
import org.springframework.http.ResponseEntity
import strikt.api.Assertion.Builder

fun Builder<ResponseEntity<String>>.hasPlnAmount(expectedPlnAmount: String): Builder<ResponseEntity<String>> =
    assert("should contain correct PLN amount") {
        val body = parseBodyToType<Map<String, Any>>(it)

        val actualPlnAmount =
            body["balancePln"] as? String ?: fail(
                "Response does not contain 'balancePln' or 'plnAmount' is not a String",
            )

        if (actualPlnAmount == expectedPlnAmount) {
            pass()
        } else {
            fail("Expected PLN amount: $expectedPlnAmount, but got $actualPlnAmount")
        }
    }

fun Builder<ResponseEntity<String>>.hasUsdAmount(expectedUsdAmount: String): Builder<ResponseEntity<String>> =
    assert("should contain correct USD amount") {
        val body = parseBodyToType<Map<String, Any>>(it)

        val actualUsdAmount =
            body["balanceUsd"] as? String ?: fail(
                "Response does not contain 'balanceUsd' or 'balanceUsd' is not a String",
            )

        if (actualUsdAmount == expectedUsdAmount) {
            pass()
        } else {
            fail("Expected USD amount: $expectedUsdAmount, but got $actualUsdAmount")
        }
    }
