package camilyed.github.io.currencyexchangeapi.api

import camilyed.github.io.currencyexchangeapi.domain.OperationId
import camilyed.github.io.currencyexchangeapi.infrastructure.InvalidHeaderException
import java.util.UUID

typealias XRequestId = String?

fun XRequestId.toOperationId() =
    try {
        OperationId(UUID.fromString(this))
    } catch (_: IllegalArgumentException) {
        throw InvalidHeaderException("X-Request-Id is required and must be a valid UUID")
    }
