package camilyed.github.io.currencyexchangeapi.domain

class InsufficientFundsException(message: String) : RuntimeException(message)

class InvalidAmountException(message: String) : RuntimeException(message)

class InvalidExchangeRateException(message: String) : RuntimeException(message)
