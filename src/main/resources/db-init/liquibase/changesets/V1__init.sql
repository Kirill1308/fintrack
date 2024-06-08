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
    user_id BIGINT       NOT NULL,
    role    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_users_roles_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS wallet
(
    id       BIGSERIAL PRIMARY KEY,
    user_id  BIGINT,
    name     VARCHAR(255),
    balance  DOUBLE PRECISION,
    currency VARCHAR(3),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS wallet_member
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGINT NOT NULL,
    wallet_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (wallet_id) REFERENCES wallet (id)
);

CREATE TABLE IF NOT EXISTS transaction
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    wallet_id    BIGINT NOT NULL,
    type         VARCHAR(255),
    category     VARCHAR(255),
    payment      VARCHAR(255),
    amount       NUMERIC,
    currency     VARCHAR(3),
    note         TEXT,
    date_created TIMESTAMP,
    date_updated TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (wallet_id) REFERENCES wallet (id)
);

CREATE TABLE invitation
(
    id           BIGSERIAL PRIMARY KEY,
    token        VARCHAR(255) NOT NULL,
    wallet_id    BIGINT       NOT NULL,
    sender_id    BIGINT       NOT NULL,
    recipient_id BIGINT       NOT NULL,
    status       VARCHAR(50)  NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wallet_id) REFERENCES wallet (id),
    FOREIGN KEY (sender_id) REFERENCES users (id),
    FOREIGN KEY (recipient_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS budget
(
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT NOT NULL,
    name             VARCHAR(255),
    budgeted_amount  DOUBLE PRECISION,
    currency         VARCHAR(255),
    category         VARCHAR(255),
    status           VARCHAR(255),
    start_date       DATE,
    end_date         DATE,
    creation_date    DATE,
    last_update_date DATE,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
