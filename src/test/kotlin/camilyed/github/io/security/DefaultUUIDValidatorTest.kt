package camilyed.github.io.security

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import java.util.UUID

class DefaultUUIDValidatorTest {

    @Test
    fun `should return secure for valid UUID when using default validator`() {
        // given
        val validUUID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
        val validator = UUIDSecureValidator.default()

        // when
        val result = validator.isSecure(validUUID)

        // then
        expectThat(result).isTrue()
    }

    @Test
    fun `should return insecure for all zeros UUID when using default validator`() {
        // given
        val allZerosUUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val validator = UUIDSecureValidator.default()

        // when
        val result = validator.isSecure(allZerosUUID)

        // then
        expectThat(result).isFalse()
    }

    @Test
    fun `should return insecure for UUID with repeating characters when using default validator`() {
        // given
        val repeatingUUID = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val validator = UUIDSecureValidator.default()

        // when
        val result = validator.isSecure(repeatingUUID)

        // then
        expectThat(result).isFalse()
    }

    @Test
    fun `should return secure for UUID with partial repeating characters when using default validator`() {
        // given
        val partialRepeatingUUID = UUID.fromString("12345678-1234-1234-5678-1234567890ab")
        val validator = UUIDSecureValidator.default()

        // when
        val result = validator.isSecure(partialRepeatingUUID)

        // then
        expectThat(result).isTrue()
    }
}
