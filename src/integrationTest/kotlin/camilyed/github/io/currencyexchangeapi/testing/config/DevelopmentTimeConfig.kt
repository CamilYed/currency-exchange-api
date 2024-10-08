package camilyed.github.io.currencyexchangeapi.testing.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean

@TestConfiguration(proxyBeanMethods = false)
class DevelopmentTimeConfig {

    @Bean
    @ServiceConnection
    fun postgresSQLContainer() = POSTGRES_SQL_CONTAINER
}
