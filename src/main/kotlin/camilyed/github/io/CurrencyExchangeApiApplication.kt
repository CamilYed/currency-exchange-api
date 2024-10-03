package camilyed.github.io

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration

@EnableFeignClients
@SpringBootApplication
@ImportAutoConfiguration(FeignAutoConfiguration::class, HttpClientConfiguration::class)
class CurrencyExchangeApiApplication

fun main(args: Array<String>) {
    runApplication<CurrencyExchangeApiApplication>(*args)
}
