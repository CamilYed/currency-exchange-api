package camilyed.github.io.currencyexchangeapi.config

import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.sql.Database
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class DatabaseConnectInitializer(private val dataSource: DataSource) {

    @PostConstruct
    fun connectDatabase() {
        Database.connect(dataSource)
    }
}
