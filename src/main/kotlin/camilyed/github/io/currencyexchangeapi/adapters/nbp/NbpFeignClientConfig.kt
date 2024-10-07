package camilyed.github.io.currencyexchangeapi.adapters.nbp

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import feign.Feign
import feign.jackson.JacksonDecoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class NbpFeignClientConfig {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    fun nbpFeignClient(@Value("\${nbp.url}") url: String): NbpFeignClient {
        return Feign.builder()
            .decoder(JacksonDecoder(jacksonObjectMapper()))
            .target(NbpFeignClient::class.java, url)
    }
}
