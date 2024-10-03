package camilyed.github.io.currencyexchangeapi.testing

import camilyed.github.io.CurrencyExchangeApiApplication
import camilyed.github.io.currencyexchangeapi.testing.abilties.MakeRequestAbility
import camilyed.github.io.currencyexchangeapi.testing.postgres.PostgresInitializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(
    initializers = [PostgresInitializer::class],
    classes = [CurrencyExchangeApiApplication::class],
)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseIntegrationTest : MakeRequestAbility {
    @Autowired
    override lateinit var restTemplate: TestRestTemplate

    private val objectMapper: ObjectMapper by lazy { jacksonObjectMapper() }

    val wireMockServer = WireMockServer(options().port(8080))

    @BeforeEach
    fun beforeEach() {
        wireMockServer.resetRequests()
    }

    override fun <T> toJson(obj: T): String {
        return objectMapper.writeValueAsString(obj)
    }

    companion object {
        lateinit var wireMockServer: WireMockServer

        @JvmStatic
        @BeforeAll
        fun startWireMockServer() {
            wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())
            wireMockServer.start()
            System.setProperty("wiremock.server.port", wireMockServer.port().toString())
        }

        @JvmStatic
        @AfterAll
        fun stopWireMockServer() {
            wireMockServer.stop()
            System.clearProperty("wiremock.server.port")
        }
    }
}
