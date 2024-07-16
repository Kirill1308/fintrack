DROP TABLE IF EXISTS budget_wallet;
DROP TABLE IF EXISTS budget;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS wallet_member;
DROP TABLE IF EXISTS wallet;
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    username      VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    phone_number  VARCHAR(255),
    date_of_birth TIMESTAMP,
    profile_image VARCHAR(255),
    gender        VARCHAR(255) NOT NULL DEFAULT 'NOT_SPECIFIED',
    status        VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id BIGSERIAL    NOT NULL,
    role    VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS wallet
(
    id       BIGSERIAL PRIMARY KEY,
    user_id  BIGINT       NOT NULL,
    name     VARCHAR(255) NOT NULL,
    balance  DOUBLE PRECISION,
    currency VARCHAR(3)   NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS wallet_member
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGINT NOT NULL,
    wallet_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (wallet_id) REFERENCES wallet (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transaction
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    wallet_id    BIGINT       NOT NULL,
    type         VARCHAR(255) NOT NULL,
    category     VARCHAR(255) NOT NULL,
    payment      VARCHAR(255) NOT NULL,
    amount       NUMERIC      NOT NULL,
    currency     VARCHAR(3)   NOT NULL,
    note         TEXT,
    date_created TIMESTAMP,
    date_updated TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (wallet_id) REFERENCES wallet (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS budget
(
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT           NOT NULL,
    name             VARCHAR(255)     NOT NULL,
    budgeted_amount  DOUBLE PRECISION NOT NULL,
    currency         VARCHAR(255)     NOT NULL,
    category         VARCHAR(255)     NOT NULL,
    status           VARCHAR(255),
    start_date       DATE,
    end_date         DATE,
    creation_date    TIMESTAMP,
    last_update_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS budget_wallet
(
    budget_id BIGINT NOT NULL,
    wallet_id BIGINT NOT NULL,
    FOREIGN KEY (budget_id) REFERENCES budget (id) ON DELETE CASCADE,
    FOREIGN KEY (wallet_id) REFERENCES wallet (id) ON DELETE CASCADE
);
