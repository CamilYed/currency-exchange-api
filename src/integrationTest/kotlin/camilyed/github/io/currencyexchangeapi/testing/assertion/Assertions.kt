package camilyed.github.io.currencyexchangeapi.testing.assertion

import camilyed.github.io.currencyexchangeapi.testing.utils.parseBodyToType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import strikt.api.Assertion
import java.util.UUID

fun <T> Assertion.Builder<ResponseEntity<T>>.isOkResponse(): Assertion.Builder<ResponseEntity<T>> =
    assert("should have a successful response status") {
        if (it.statusCode == HttpStatus.OK || it.statusCode == HttpStatus.CREATED) {
            pass()
        } else {
            fail("Expected OK or CREATED, but got ${it.statusCode}")
        }
    }

fun Assertion.Builder<ResponseEntity<String>>.hasUUID(): Assertion.Builder<ResponseEntity<String>> =
    assert("should contain a valid UUID in 'id' field") {
        val body = parseBodyToType<Map<String, Any>>(it)
        val actualId =
            body["id"] as? String ?: fail("Response does not contain 'id' or 'id' is not a String")
        try {
            UUID.fromString(actualId as String)
            pass()
        } catch (e: IllegalArgumentException) {
            fail("The 'id' is not a valid UUID: $actualId")
        }
    }

fun <T> Assertion.Builder<ResponseEntity<T>>.isBadRequest(): Assertion.Builder<ResponseEntity<T>> =
    assert("should have a BAD REQUEST response status") {
        if (it.statusCode == HttpStatus.BAD_REQUEST) {
            pass()
        } else {
            fail("Expected BAD REQUEST, but got ${it.statusCode}")
        }
    }

fun <T> Assertion.Builder<ResponseEntity<T>>.isUnprocessableEntity(): Assertion.Builder<ResponseEntity<T>> =
    assert("should have an UNPROCESSABLE ENTITY response status") {
        if (it.statusCode == HttpStatus.UNPROCESSABLE_ENTITY) {
            pass()
        } else {
            fail("Expected UNPROCESSABLE ENTITY, but got ${it.statusCode}")
        }
    }

fun Assertion.Builder<ResponseEntity<String>>.hasProblemDetail(
    expectedField: String,
    expectedDetail: String,
): Assertion.Builder<ResponseEntity<String>> =
    assert("should contain problem detail, field '$expectedField' with value '$expectedDetail'") {
        val body = parseBodyToType<Map<String, Any>>(it)
        val actualDetail =
            body["detail"] as? String ?: fail("No 'detail' field in response body") as String
        if (actualDetail.contains("$expectedField: $expectedDetail")) {
            pass()
        } else {
            fail(
                "Expected error detail for field " +
                    "'$expectedField' with message " +
                    "'$expectedDetail', " +
                    "but got '$actualDetail'",
            )
        }
    }
