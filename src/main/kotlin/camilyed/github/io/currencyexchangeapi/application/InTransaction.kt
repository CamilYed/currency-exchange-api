package camilyed.github.io.currencyexchangeapi.application

var inTransaction: (() -> Any) -> Any = { block ->
    block()
}
