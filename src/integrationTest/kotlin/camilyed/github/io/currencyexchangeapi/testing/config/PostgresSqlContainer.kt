package camilyed.github.io.currencyexchangeapi.testing.config

import org.testcontainers.containers.PostgreSQLContainer

val POSTGRES_SQL_CONTAINER = PostgreSQLContainer("postgres:13.4-alpine")
    .apply {
        withDatabaseName("test_db")
        withUsername("test_user")
        withPassword("test_password")
        withInitScript("init.sql")
    }
