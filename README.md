# Currency Exchange API (PLN <-> USD)

## Overview

This is a Kotlin-based Spring Boot application that provides a REST API for creating currency accounts and performing currency exchanges between PLN (Polish Zloty) and USD (US Dollar). The exchange rates are retrieved from the National Bank of Poland (NBP) public API.

## Functional Requirements

1. The application exposes a REST API for creating currency accounts.
2. When creating an account, the user must provide the initial balance in PLN.
3. The user must also provide their full name (first and last name).
4. The application generates a unique account identifier when a new account is created. This identifier is used in further API interactions.
5. The application exposes a REST API for exchanging money between PLN and USD, fetching the current exchange rate from the public NBP API.
6. The application exposes a REST API for retrieving account details, including the current balance in PLN and USD.

## Non-Functional Requirements

1. The application is written in Kotlin and uses the Spring Boot framework.
2. The application does not persist data after a restart (in-memory storage).
3. The source code is hosted on a version control platform like GitHub, GitLab, or Bitbucket.
4. The application is built using a build tool such as Maven or Gradle.
5. Any unspecified details are left to the developer's discretion.
6. If any questions arise, clarification should be requested by email.

## Technologies

- **Language**: Kotlin
- **Framework**: Spring Boot
- **Build tool**: Gradle
- **Testing**: JUnit 5
- **External API**: NBP public API for exchange rates [http://api.nbp.pl/](http://api.nbp.pl/)
