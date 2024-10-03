CREATE TABLE accounts
(
    id          UUID PRIMARY KEY NOT NULL,
    owner       VARCHAR(255)     NOT NULL,
    balance_pln NUMERIC(20, 2)   NOT NULL,
    balance_usd NUMERIC(20, 2)   NOT NULL
);
