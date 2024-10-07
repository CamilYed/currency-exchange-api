package camilyed.github.io.currencyexchangeapi.testing.builders

class CreateAccountJsonBuilder {
    private var owner: String? = "John Doe"
    private var initialBalance: String? = "1000.00"
    var xRequestId: String = "05df4b53-1d60-420b-9b46-63c867c0dd02"

    fun withOwner(owner: String?) =
        apply {
            this.owner = owner
        }

    fun withInitialBalance(initialBalance: String?) =
        apply {
            this.initialBalance = initialBalance
        }

    fun withXRequestId(id: String) = apply { this.xRequestId = id }

    fun build(): Map<String, Any?> {
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
