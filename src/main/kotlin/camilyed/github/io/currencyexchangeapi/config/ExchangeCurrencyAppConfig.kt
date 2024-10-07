package camilyed.github.io.currencyexchangeapi.config

import camilyed.github.io.currencyexchangeapi.application.AccountService
import camilyed.github.io.currencyexchangeapi.domain.AccountOperationRepository
import camilyed.github.io.currencyexchangeapi.domain.AccountRepository
import camilyed.github.io.currencyexchangeapi.domain.CurrentExchangeRateProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExchangeCurrencyAppConfig {

    @Bean
    fun accountService(
        accountRepository: AccountRepository,
        currentExchangeRateProvider: CurrentExchangeRateProvider,
        accountOperationRepository: AccountOperationRepository,
    ) = AccountService(
        accountRepository,
        currentExchangeRateProvider,
        accountOperationRepository,
    )
}
