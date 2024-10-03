package camilyed.github.io.currencyexchangeapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class DataSourceConfig {
    @Bean
    fun testDataSource(
        @Value("\${spring.datasource.url}") url: String,
        @Value("\${spring.datasource.username}") username: String,
        @Value("\${spring.datasource.password}") password: String,
    ): DataSource {
        return DriverManagerDataSource().apply {
            setDriverClassName("org.postgresql.Driver")
            setUrl(url)
            setUsername(username)
            setPassword(password)
        }
    }
}
