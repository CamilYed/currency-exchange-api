package camilyed.github.io.security

import java.util.UUID
import kotlin.math.ln

interface UUIDSecureValidator {
    fun isSecure(uuid: UUID): Boolean

    companion object {
        fun default(): UUIDSecureValidator = DefaultUUIDValidator

        fun configurable(entropyThreshold: Double): UUIDSecureValidator = ConfigurableUUIDValidator(entropyThreshold)
    }
}

private object DefaultUUIDValidator : UUIDSecureValidator {

    override fun isSecure(uuid: UUID): Boolean {
        return isValidUUID(uuid, DEFAULT_ENTROPY_THRESHOLD)
    }
}

private class ConfigurableUUIDValidator(private val entropyThreshold: Double) : UUIDSecureValidator {
    override fun isSecure(uuid: UUID): Boolean {
        return isValidUUID(uuid, entropyThreshold)
    }
}

private fun isValidUUID(uuid: UUID, entropyThreshold: Double): Boolean {
    val uuidString = uuid.toString().replace("-", "")

    if (uuidString.length != UUID_LENGTH_WITHOUT_DASHES) {
        return false
    }

    val firstChar = uuidString[0]
    val allSameChars = uuidString.all { it == firstChar }
    if (allSameChars) return false

    val entropy = calculateEntropy(uuidString)
    return entropy > entropyThreshold
}

private fun calculateEntropy(input: String): Double {
    val charCounts = input.groupingBy { it }.eachCount()
    val length = input.length
    val logBase = 2.0
    return charCounts.values.fold(0.0) { entropy, count ->
        val probability = count.toDouble() / length
        entropy - (probability * (ln(probability) / ln(logBase)))
    }
}
private const val DEFAULT_ENTROPY_THRESHOLD = 3.0
private const val UUID_LENGTH_WITHOUT_DASHES = 32
