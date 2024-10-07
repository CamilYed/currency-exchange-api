package camilyed.github.io.currencyexchangeapi.testing

import camilyed.github.io.CurrencyExchangeApiApplication
import camilyed.github.io.currencyexchangeapi.testing.abilties.MakeRequestAbility
import camilyed.github.io.currencyexchangeapi.testing.postgres.PostgresInitializer
import camilyed.github.io.currencyexchangeapi.testing.utils.DatabaseCleaner
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

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

    @BeforeEach
    fun beforeEach() {
        wireMock.resetAll()
        DatabaseCleaner.cleanAllTables()
    }

    override fun <T> toJson(obj: T): String {
        return objectMapper.writeValueAsString(obj)
    }

    companion object {
        protected val wireMock = WireMockServer(0)
            .apply {
                start()
                println("WireMock Started")
            }
            .also { WireMock.configureFor(it.port()) }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("wiremock.server.port", wireMock::port)
        }

        @JvmStatic
        @BeforeAll
        fun startWireMock() {
            if (!wireMock.isRunning) {
                wireMock.start()
                WireMock.configureFor(wireMock.port())
                System.clearProperty("wiremock.server.port")
            }
        }

        @JvmStatic
        @AfterAll
        fun stopWireMockServer() {
            wireMock.stop()
            System.clearProperty("wiremock.server.port")
        }
    }
}
