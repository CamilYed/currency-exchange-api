package camilyed.github.io.currencyexchangeapi

import camilyed.github.io.CurrencyExchangeApiApplication
import camilyed.github.io.currencyexchangeapi.testing.config.DevelopmentTimeConfig
import org.springframework.boot.builder.SpringApplicationBuilder

fun main(args: Array<String>) {
    SpringApplicationBuilder()
        .sources(CurrencyExchangeApiApplication::class.java, DevelopmentTimeConfig::class.java)
        .profiles("test")
        .run(*args)
}
