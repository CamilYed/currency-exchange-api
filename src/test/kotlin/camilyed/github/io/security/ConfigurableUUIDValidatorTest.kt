package camilyed.github.io.security

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import java.util.UUID

class ConfigurableUUIDValidatorTest {

    @Test
    fun `should return secure for valid UUID with custom entropy threshold`() {
        // given
        val validUUID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
        val validator = UUIDSecureValidator.configurable(2.5)

        // when
        val result = validator.isSecure(validUUID)

        // then
        expectThat(result).isTrue()
    }

    @Test
    fun `should return secure for low entropy UUID with lower entropy threshold`() {
        // given
        val lowEntropyUUID = UUID.fromString("12341234-1234-1234-5678-1234567890ab")
        val validator = UUIDSecureValidator.configurable(1.5)

        // when
        val result = validator.isSecure(lowEntropyUUID)

        // then
        expectThat(result).isTrue()
    }

    @Test
    fun `should return insecure for valid UUID with high entropy threshold`() {
        // given
        val validUUID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
        val validator = UUIDSecureValidator.configurable(5.0)

        // when
        val result = validator.isSecure(validUUID)

        // then
        expectThat(result).isFalse()
    }

    @Test
    fun `should return insecure for low entropy UUID with high entropy threshold`() {
        // given
        val lowEntropyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val validator = UUIDSecureValidator.configurable(5.0)

        // when
        val result = validator.isSecure(lowEntropyUUID)

        // then
        expectThat(result).isFalse()
    }
}
