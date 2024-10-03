package camilyed.github.io.currencyexchangeapi.domain

class AccountNotFoundException(message: String) : RuntimeException(message)

class InsufficientFundsException(message: String) : RuntimeException(message)

class InvalidAmountException(message: String) : RuntimeException(message)

class InvalidExchangeRateException(message: String) : RuntimeException(message)
