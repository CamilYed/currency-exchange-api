package camilyed.github.io.currencyexchangeapi.testing.builders

class CreateAccountJsonBuilder {
    private var owner: String = "John Doe"
    private var initialBalance: String = "1000.00"

    fun withOwner(owner: String) =
        apply {
            this.owner = owner
        }

    fun withInitialBalance(initialBalance: String) =
        apply {
            this.initialBalance = initialBalance
        }

    fun build(): Map<String, Any> {
        return mapOf(
            "owner" to owner,
            "initialBalance" to initialBalance,
        )
    }

    companion object {
        fun aCreateAccount(): CreateAccountJsonBuilder {
            return CreateAccountJsonBuilder()
        }
    }
}
