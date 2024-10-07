package camilyed.github.io.currencyexchangeapi.testing.config

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class WireMockConfig {

    @Bean
    @Primary
    fun wireMockServer(): WireMockServer {
        val wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())
        wireMockServer.start()
        return wireMockServer
    }
}
