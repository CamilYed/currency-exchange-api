package camilyed.github.io.currencyexchangeapi.infrastructure

import mu.KotlinLogging
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Profile
import org.testcontainers.containers.PostgreSQLContainer

@Profile("!test")
class PostgresInitializer : ApplicationContextInitializer<ConfigurableApplicationContext>, DisposableBean {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        log.info("Overriding system properties")
        overrideSystemProperties()
    }

    override fun destroy() {
        if (pg.isRunning) {
            log.info("Stopping PostgreSQL container...")
            pg.stop()
        }
    }

    private fun overrideSystemProperties() {
        System.setProperty("spring.datasource.url", pg.jdbcUrl)
        System.setProperty("spring.datasource.username", pg.username)
        System.setProperty("spring.datasource.password", pg.password)
    }

    private companion object {
        private val log = KotlinLogging.logger {}
        private val pg: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:13.4-alpine")
                .apply {
                    withReuse(true)
                    withDatabaseName("test_db")
                    withUsername("test_user")
                    withPassword("test_password")
                }

        init {
            pg.withInitScript("init.sql")
            pg.start()
            log.info("Postgres started")
        }
    }
}
