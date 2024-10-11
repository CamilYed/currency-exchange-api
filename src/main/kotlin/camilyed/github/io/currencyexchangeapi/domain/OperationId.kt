package camilyed.github.io.currencyexchangeapi.domain

import camilyed.github.io.security.UUIDSecureValidator
import java.util.UUID

@JvmInline
value class OperationId(val value: UUID) {

    init {
        require(UUIDSecureValidator.default().isSecure(value)) {
            throw InsecureOperationException("Insecure operation: UUID $value does not meet security requirements.")
        }
    }
}
