package camilyed.github.io.currencyexchangeapi.testing.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.ResponseEntity

val objectMapper = jacksonObjectMapper()

inline fun <reified T> parseBodyToType(response: ResponseEntity<String>): T {
    return objectMapper.readValue(response.body!!, T::class.java)
}
