CREATE TABLE accounts
(
    id          UUID PRIMARY KEY NOT NULL,
    owner       VARCHAR(255)     NOT NULL,
    balance_pln NUMERIC(20, 2)   NOT NULL,
    balance_usd NUMERIC(20, 2)   NOT NULL
);

CREATE TABLE account_operations
(
    id             UUID PRIMARY KEY NOT NULL,
    account_id     UUID             NOT NULL,
    operation_type VARCHAR(255)     NOT NULL,
    operation_id   UUID             NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount_pln     NUMERIC(20, 2),
    amount_usd     NUMERIC(20, 2),
    exchange_rate  NUMERIC(10, 4),
    description    TEXT
);
