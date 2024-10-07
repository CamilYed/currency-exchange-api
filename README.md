
# Currency Exchange API (PLN <-> USD)

## Overview

This is a Kotlin-based Spring Boot application that provides a REST API for creating currency accounts and performing currency exchanges between PLN (Polish Zloty) and USD (US Dollar). The exchange rates are retrieved from the National Bank of Poland (NBP) public API.

The project follows **Domain-Driven Design (DDD)** principles, where core domain logic is encapsulated in domain aggregates, ensuring that the business logic is well-organized and scalable. The endpoints provided by the application are idempotent, ensuring consistency of operations, even when they are retried.

## Features

1. **Account Management**:
    - REST API for creating currency accounts.
    - When creating an account, the user must provide an initial balance in PLN and their full name.
    - A unique account identifier is generated for future interactions.

2. **Currency Exchange**:
    - REST API for exchanging currencies between PLN and USD, using live exchange rates from the NBP API.

3. **Balance Retrieval**:
    - REST API for retrieving account details, including current balances in PLN and USD.

## Technologies

- **Language**: Kotlin
- **Framework**: Spring Boot
- **Build Tool**: Gradle
- **External API**: NBP API for exchange rates
- **Testing**: JUnit 5, tests based on the [Readable Tests by Example](https://blog.allegro.tech/2022/02/readable-tests-by-example.html) approach to improve readability and maintainability.

## Architecture

The application is structured according to **Domain-Driven Design (DDD)**. It contains well-defined aggregates that represent the key business entities, ensuring that domain logic and data consistency are managed centrally. Here's an example from the `Account` aggregate:

```kotlin
class Account(
    val id: AccountId,
    val name: String,
    var balancePLN: BigDecimal,
    var balanceUSD: BigDecimal
) {
    fun exchangeCurrency(amount: BigDecimal, exchangeRate: BigDecimal) {
        // Business logic to handle currency exchange
    }
}
```

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

3. Run tests:
   ```bash
   ./gradlew check
   ```
   
4. The application will be accessible at `http://localhost:8080`.

## TODO

The following features and tests are still missing or require improvements:

1. **Missing REST API for Account Details**:
    - Endpoint for retrieving account details (current balances in PLN and USD).
    - Tests for the account details API.

2. **Missing Exchange Endpoint (USD to PLN)**:
    - An additional API endpoint for currency exchange from USD to PLN.
    - Tests to cover this exchange scenario.

3. **Optimistic Locking Tests**:
    - Currently, there are no tests to validate how optimistic locking is handled during concurrent transactions.

4. **Error Handling Tests**:
    - Tests to handle scenarios where the NBP API is unavailable (e.g., simulate HTTP 500 responses and ensure that FeignClient retries or fallback mechanisms work as expected).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
