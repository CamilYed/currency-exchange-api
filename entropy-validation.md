### Entropy-Based UUID Validation Algorithm

This section explains the step-by-step process used to validate the security of UUIDs based on entropy. The goal is to ensure that the `X-Request-Id` used in operations is secure and unpredictable.

1. **Remove Formatting**: The UUID is initially formatted with dashes (e.g., `123e4567-e89b-12d3-a456-426614174000`). For entropy calculation, these dashes are removed, leaving a 32-character string consisting of hexadecimal characters (0-9, a-f).  
   Example:
    - Original UUID: `123e4567-e89b-12d3-a456-426614174000`
    - Formatted for entropy: `123e4567e89b12d3a456426614174000`
---
2. **Character Frequency Count**: The algorithm counts how many times each character appears in the 32-character string. This count helps determine how evenly distributed the characters are, which is important for measuring randomness.
---
3. **Probability Calculation**: For each unique character in the string, the algorithm calculates the probability of occurrence using the formula:  
   `p_i = count_of_character / total_length_of_string (32)`  
   Where `p_i` is the probability of each character.
---
4. **Entropy Calculation**: Using the calculated probabilities, the entropy is computed based on Shannon's entropy formula:  
   `Entropy = -Σ (p_i * log2(p_i))`  
   Where:
    - `p_i` is the probability of each character in the string.
    - The logarithm is taken with base 2, as entropy is typically measured in bits.
---
5. **Threshold Comparison**: Once the entropy is calculated, it is compared against a predefined threshold (default: **3.0**). A higher entropy value indicates a more random and secure UUID, while a lower entropy suggests the UUID may be predictable or weak.
---
6. **Validation**: If the entropy of the UUID is greater than the threshold, the UUID is considered **secure**. Otherwise, the operation is rejected with a `400 Bad Request` response, indicating that the `X-Request-Id` does not meet security requirements.


### Code
```kotlin
fun isValidUUID(uuid: UUID, entropyThreshold: Double): Boolean {
    val uuidString = uuid.toString().replace("-", "")

    val isInvalid = uuidString.length != UUID_LENGTH_WITHOUT_DASHES ||
        uuidString.all { it == uuidString[0] } ||
        calculateEntropy(uuidString) <= entropyThreshold

    return !isInvalid
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
```

### Why Entropy Matters

Entropy measures the randomness and unpredictability of data. For UUIDs, high entropy ensures that each UUID is unique and not easily guessable. Low-entropy UUIDs, such as those with repeating characters or patterns, can be exploited, leading to potential security vulnerabilities. By 
