package camilyed.github.io.currencyexchangeapi.testing

import camilyed.github.io.CurrencyExchangeApiApplication
import camilyed.github.io.currencyexchangeapi.testing.abilties.MakeRequestAbility
import camilyed.github.io.currencyexchangeapi.testing.config.POSTGRES_SQL_CONTAINER
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
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@ContextConfiguration(
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

        @ServiceConnection
        val postgresSQLContainer = POSTGRES_SQL_CONTAINER

        private val wireMock = WireMockServer(0)

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("nbp.url") { "http://localhost:${wireMock.port()}/api/exchangerates/rates/A/USD" }
        }

        @JvmStatic
        @BeforeAll
        fun start() {
            startWiremock()
            startPostgresContainer()
        }

        private fun startWiremock() {
            if (!wireMock.isRunning) {
                println("Starting Wiremock...")
                wireMock.start()
                WireMock.configureFor(wireMock.port())
                println("Wiremock started at port: ${wireMock.port()}")
            }
        }

        private fun startPostgresContainer() {
            if (!postgresSQLContainer.isRunning) {
                println("Starting PostgresContainer...")
                postgresSQLContainer.start()
                println("PostgresContainer started")
            }
        }

        init {
            addShutdownHooks()
        }

        private fun addShutdownHooks() {
            Runtime.getRuntime().addShutdownHook(
                Thread {
                    shutdownWiremock()
                    shutdownPostgresContainer()
                },
            )
        }

        private fun shutdownWiremock() {
            if (wireMock.isRunning) {
                println("Shutting down WireMock server...")
                wireMock.stop()
                println("WireMock stopped")
            }
        }

        private fun shutdownPostgresContainer() {
            if (postgresSQLContainer.isRunning) {
                println("Shutting down PostgresContainer...")
                postgresSQLContainer.stop()
                println("PostgresContainer stopped")
            }
        }
    }
}
