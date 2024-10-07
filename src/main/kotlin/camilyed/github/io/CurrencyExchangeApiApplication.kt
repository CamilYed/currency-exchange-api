package camilyed.github.io

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration

@EnableFeignClients
@SpringBootApplication
@ImportAutoConfiguration(FeignAutoConfiguration::class)
class CurrencyExchangeApiApplication

fun main(args: Array<String>) {
    runApplication<CurrencyExchangeApiApplication>(*args)
}
