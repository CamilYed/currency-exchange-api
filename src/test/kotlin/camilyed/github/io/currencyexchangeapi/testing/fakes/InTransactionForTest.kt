package camilyed.github.io.currencyexchangeapi.testing.fakes

fun <T> inTransactionForTests(block: () -> T): T {
    return block()
}
