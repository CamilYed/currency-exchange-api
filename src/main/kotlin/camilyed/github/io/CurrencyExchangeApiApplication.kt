package camilyed.github.io

import camilyed.github.io.currencyexchangeapi.infrastructure.PostgresInitializer
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration

@EnableFeignClients
@SpringBootApplication
@ImportAutoConfiguration(FeignAutoConfiguration::class)
class CurrencyExchangeApiApplication

fun main(args: Array<String>) {
    val application = SpringApplication(CurrencyExchangeApiApplication::class.java)
    application.addInitializers(PostgresInitializer())
    application.run(*args)
}
