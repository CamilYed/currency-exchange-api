package camilyed.github.io.currencyexchangeapi.testing

import camilyed.github.io.CurrencyExchangeApiApplication
import camilyed.github.io.currencyexchangeapi.testing.abilties.MakeRequestAbility
import camilyed.github.io.currencyexchangeapi.testing.postgres.PostgresInitializer
import camilyed.github.io.currencyexchangeapi.testing.utils.DatabaseCleaner
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
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
        WireMock.reset()
        DatabaseCleaner.cleanAllTables()
    }

    override fun <T> toJson(obj: T): String {
        return objectMapper.writeValueAsString(obj)
    }

    companion object {
        protected val wireMock = WireMockServer(0)

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("nbp.url") { "http://localhost:${wireMock.port()}/api/exchangerates/rates/A/USD" }
        }

        @JvmStatic
        @BeforeAll
        fun startWireMock() {
            if (!wireMock.isRunning) {
                wireMock.start()
                WireMock.configureFor(wireMock.port())
                println("WIREMOCK STARTED at port: ${wireMock.port()}")
            }
        }

        init {
            Runtime.getRuntime().addShutdownHook(
                Thread {
                    if (wireMock.isRunning) {
                        println("Shutting down WireMock server...")
                        wireMock.stop()
                        println("WireMock stopped")
                    }
                },
            )
        }
    }
}
