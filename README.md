
# Currency Exchange API (PLN <-> USD)

## Overview

This is a Kotlin-based Spring Boot application implementing a domain-driven design (DDD) architecture to handle currency exchange operations between PLN (Polish Zloty) and USD (US Dollar). The exchange rates are retrieved from the National Bank of Poland (NBP) API.

The application is designed with aggregates, domain events, and history tracking of account operations, ensuring idempotent, consistent, and traceable transactions.

## Features

1. **Account Management**:
    - API for creating new currency accounts.
    - Each account tracks balances in PLN and USD.
    - Every account creation requires a unique `X-Request-Id`, ensuring idempotency.

2. **Currency Exchange**:
    - API to exchange PLN to USD based on live exchange rates from NBP.
    - Each exchange operation is idempotent using `X-Request-Id` as a unique operation identifier.
    - Exchange history is recorded as domain events.

3. **Operation History**:
    - The application tracks the full history of account operations, including currency exchanges, ensuring traceability of all actions.
    - Events such as account creation and currency exchanges are stored as immutable records.

4. **Resiliency**:
    - Built-in error handling for NBP API unavailability using FeignClient retries.
    - Idempotency ensures consistent results across retries or repeated operations.

## Architecture

The project follows **Domain-Driven Design (DDD)** principles, which structure the application around domain aggregates that encapsulate business rules and behavior. This approach ensures that core business logic is always centralized and preserved. Below is an example of how the `Account` aggregate works, particularly focusing on currency exchange operations.

### Transaction Management Example (Lambda):
Transaction management is handled using a lambda function that wraps database operations in a transactional context.

```kotlin
fun <T> executeInTransaction(block: () -> T): T {
    return inTransaction(block as () -> Any) as T
}

private var inTransaction: (() -> Any) -> Any = { block -> block() }

@Configuration
class TransactionManagerConfig {

    @PostConstruct
    fun setupProductionTransaction() {
        inTransaction = { block -> transaction { block() } }
    }
}
```

#### Example from `AccountService`:

```kotlin
fun create(command: CreateAccountCommand): AccountSnapshot {
    val accountId = accountOperationRepository.findAccountIdBy(command.commandId)
    if (accountId != null) {
        return executeInTransaction { findAccount(accountId).toSnapshot() }
    }
    val id = repository.nextAccountId()
    val account = Account.createNewAccount(command.toCreateAccountData(id))
    executeInTransaction {
        repository.save(account)
        val events = account.getEvents()
        accountOperationRepository.save(events)
    }
    return account.toSnapshot()
}
```

This setup is used in the `AccountService` class, for example, during account creation and currency exchange.

### Example of the `Account` Aggregate (Currency Exchange):

```kotlin
class Account private constructor(
    private val id: UUID,
    private val owner: String,
    private var balancePln: Money = Money(BigDecimal.ZERO, "PLN"),
    private var balanceUsd: Money = Money(BigDecimal.ZERO, "USD"),
) {
    private val events = mutableListOf<AccountEvent>()

    init {
        require(balancePln.currency == "PLN") { "PLN balance must be in PLN" }
        require(balanceUsd.currency == "USD") { "USD balance must be in USD" }
    }

    fun exchangePlnToUsd(
        amountPln: Money,
        exchangeRate: ExchangeRate,
        operationId: UUID,
    ) {
        require(!amountPln.isZero()) {
            throw InvalidAmountException("Amount must be greater than 0")
        }
        require(amountPln <= balancePln) {
            throw InsufficientFundsException(
                "Insufficient PLN balance",
            )
        }

        val amountUsd = Money(exchangeRate.convertFromPln(amountPln.amount), "USD")
        balancePln -= amountPln
        balanceUsd += amountUsd

        addEvent(
            AccountEvent.PlnToUsdExchangeEvent(
                accountId = id,
                operationId = operationId,
                amountPln = amountPln.amount,
                amountUsd = amountUsd.amount,
                exchangeRate = exchangeRate.rate,
            ),
        )
    }

    fun exchangeUsdToPln(amountUsd: Money, exchangeRate: ExchangeRate) {
        require(!amountUsd.isZero()) {
            throw InvalidAmountException("Amount must be greater than 0")
        }
        require(amountUsd <= balanceUsd) {
            throw InsufficientFundsException(
                "Insufficient USD balance",
            )
        }

        val amountPln = Money(exchangeRate.convertToPln(amountUsd.amount), "PLN")
        balanceUsd -= amountUsd
        balancePln += amountPln
    }

    private fun addEvent(event: AccountEvent) {
        events.add(event)
    }

    fun getEvents(): List<AccountEvent> = events.toList()

    fun toSnapshot(): AccountSnapshot {
        return AccountSnapshot(
            id = id,
            owner = owner,
            balancePln = balancePln.amount,
            balanceUsd = balanceUsd.amount,
        )
    }

    companion object {
        fun createNewAccount(data: CreateAccountData): Account {
            val account = Account(
                id = data.id,
                owner = data.owner,
                balancePln = Money.pln(data.initialBalancePln),
                balanceUsd = Money.usd(BigDecimal.ZERO),
            )
            account.addEvent(
                AccountEvent.AccountCreatedEvent(
                    accountId = data.id,
                    operationId = data.operationId,
                    owner = data.owner,
                    initialBalancePln = data.initialBalancePln,
                ),
            )
            return account
        }

        fun fromSnapshot(snapshot: AccountSnapshot): Account {
            return Account(
                id = snapshot.id,
                owner = snapshot.owner,
                balancePln = Money.pln(snapshot.balancePln),
                balanceUsd = Money.usd(snapshot.balanceUsd),
            )
        }
    }
}

data class AccountSnapshot(
    val id: UUID,
    val owner: String,
    val balancePln: BigDecimal,
    val balanceUsd: BigDecimal,
)
```

### Domain Events

Events such as `PlnToUsdExchangeEvent` are stored in the history of the account to maintain a record of all operations performed.

```kotlin
sealed class AccountEvent(
    open val accountId: UUID,
    open val operationId: UUID,
) {
    data class AccountCreatedEvent(
        override val accountId: UUID,
        override val operationId: UUID,
        val owner: String,
        val initialBalancePln: BigDecimal,
    ) : AccountEvent(accountId, operationId)

    data class PlnToUsdExchangeEvent(
        override val accountId: UUID,
        override val operationId: UUID,
        val amountPln: BigDecimal,
        val amountUsd: BigDecimal,
        val exchangeRate: BigDecimal,
    ) : AccountEvent(accountId, operationId)
}
```

These events allow us to reconstruct the account's state and trace every operation performed.

### Idempotency

All key operations (like account creation and currency exchange) are idempotent, meaning that repeated requests with the same `X-Request-Id` will yield the same result without duplicating the action.

## Tests

The project includes integration tests that use a DSL to improve readability and maintainability, inspired by [Allegro's blog post on readable tests](https://blog.allegro.tech/2022/02/readable-tests-by-example.html).

### Example of a Test with DSL:

```kotlin
@Test
fun `should exchange PLN to USD successfully`() {
    // given
    val accountId = thereIsAnAccount(aCreateAccount().withInitialBalance("1000.00"))

    // and
    currentExchangeRateIs("4.0")

    // when
    val response = exchangePlnToUsd(
        anExchangePlnToUsd()
            .withAccountId(accountId)
            .withAmount("400"),
    )

    // then
    expectThat(response)
        .isOkResponse()
        .hasPlnAmount("600.00")
        .hasUsdAmount("100.00")
}
```

In this example, the test is written in a readable DSL style, ensuring clarity and maintaining a focus on business logic rather than technical details.

## Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/CamilYed/currency-exchange-api.git
   cd currency-exchange-api
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

4. The application will be accessible at `http://localhost:8080`.

## Running Tests

To run the test suite, including the integration tests, use:

```bash
./gradlew check
```

This will execute all the tests and ensure that the application is functioning as expected.

## HTTP Request Files

The project includes HTTP request files that can be used in IntelliJ IDEA for manual testing. These files contain pre-built requests for creating accounts and performing currency exchanges.

### Example Request - Create Account:
```http
POST http://localhost:8080/api/accounts
Content-Type: application/json
X-Request-Id: {{requestId}}

{
  "owner": "John Doe",
  "initialBalance": "1000.00"
}
```

### Example Request - Exchange PLN to USD:
```http
PUT http://localhost:8080/api/accounts/exchange-pln-to-usd
Content-Type: application/json
X-Request-Id: {{requestId}}

{
  "accountId": "{{accountId}}",
  "amount": "100.00"
}
```

These requests can be found in the `http-requests` directory and executed directly in IntelliJ IDEA for quick manual testing.

## TODO

The following features and tests are still missing or require improvements:

1. **REST API for Account Details**:
    - Endpoint for retrieving account details, including current balances in PLN and USD.
    - Tests for the account details API.

2. **Missing USD to PLN Exchange Endpoint**:
    - An additional API endpoint for currency exchange from USD to PLN.
    - Tests to cover this exchange scenario.

3. **Optimistic Locking Tests**:
    - Tests to validate optimistic locking during concurrent transactions.

4. **Error Handling Tests**:
    - Tests to ensure that when the NBP API is unavailable, proper error handling and retry mechanisms work as expected.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

