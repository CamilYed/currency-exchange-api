package camilyed.github.io.currencyexchangeapi.testing.abilties

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

interface MakeRequestAbility {
    val restTemplate: TestRestTemplate

    fun <T> get(
        url: String,
        responseType: Class<T>,
    ): ResponseEntity<T> {
        return restTemplate.getForEntity(url, responseType)
    }

    fun <T> post(
        url: String,
        body: Map<String, Any?>,
        headers: HttpHeaders,
        responseType: Class<T>,
    ): ResponseEntity<T> {
        val jsonString = toJson(body)
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(jsonString, headers)
        return restTemplate.postForEntity(url, requestEntity, responseType)
    }

    fun <T> toJson(obj: T): String
}
