SET SCHEMA fintrack_test;

INSERT INTO users (name, username, password, phone_number, date_of_birth, profile_image, gender, status)
VALUES ('John Doe', 'john.doe@example.com', 'password123', '1234567890', '1990-01-01', 'profile.jpg', 'MALE', 'ACTIVE'),
       ('Jane Smith', 'jane.smith@example.com', 'password123', '0987654321', '1992-02-02', 'profile2.jpg', 'FEMALE',
        'ACTIVE'),
       ('Alice Johnson', 'alice.johnson@example.com', 'password123', '1231231234', '1985-03-03', NULL, 'FEMALE',
        'ACTIVE'),
       ('Bob Brown', 'bob.brown@example.com', 'password123', '4321432143', '1988-04-04', NULL, 'MALE', 'ACTIVE'),
       ('Charlie Davis', 'charlie.davis@example.com', 'password123', '5678567856', '1995-05-05', NULL, 'MALE',
        'ACTIVE');

INSERT INTO users_roles (user_id, role)
VALUES (1, 'ROLE_USER'),
       (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER'),
       (4, 'ROLE_USER'),
       (5, 'ROLE_USER');

INSERT INTO wallet (user_id, name, balance, currency)
VALUES (1, 'Personal Wallet', 1000.0, 'USD'),
       (2, 'Travel Wallet', 2000.0, 'USD'),
       (3, 'Savings Wallet', 5000.0, 'USD'),
       (4, 'Groceries Wallet', 300.0, 'USD'),
       (5, 'Entertainment Wallet', 150.0, 'USD');

INSERT INTO wallet_member (user_id, wallet_id)
VALUES (1, 2),
       (3, 2),
       (4, 1),
       (5, 3);

INSERT INTO transaction (user_id, wallet_id, type, category, payment, amount, currency, note, date_created,
                         date_updated)
VALUES (1, 1, 'EXPENSE', 'Groceries', 'Credit Card', 50.0, 'USD', 'Grocery shopping', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (2, 2, 'INCOME', 'Salary', 'Bank Transfer', 1500.0, 'USD', 'Monthly salary', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (3, 3, 'EXPENSE', 'Rent', 'Bank Transfer', 1000.0, 'USD', 'Monthly rent payment', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (4, 4, 'EXPENSE', 'Groceries', 'Cash', 75.0, 'USD', 'Weekly groceries', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (5, 5, 'EXPENSE', 'Entertainment', 'Credit Card', 40.0, 'USD', 'Movies', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 2, 'EXPENSE', 'Travel', 'Credit Card', 500.0, 'USD', 'Flight tickets', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 2, 'EXPENSE', 'Travel', 'Cash', 300.0, 'USD', 'Hotel booking', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 1, 'INCOME', 'Freelance', 'Bank Transfer', 200.0, 'USD', 'Freelance project', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (5, 3, 'INCOME', 'Gift', 'Cash', 100.0, 'USD', 'Birthday gift', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 5, 'EXPENSE', 'Dining', 'Credit Card', 60.0, 'USD', 'Restaurant dinner', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

INSERT INTO budget (user_id, name, budgeted_amount, currency, category, status, start_date, end_date, creation_date,
                    last_update_date)
VALUES (1, 'Monthly Budget', 1000.0, 'USD', 'General', 'ACTIVE', '2024-06-01', '2024-06-30', CURRENT_DATE,
        CURRENT_DATE),
       (2, 'Vacation Budget', 2000.0, 'USD', 'Travel', 'ACTIVE', '2024-07-01', '2024-07-31', CURRENT_DATE,
        CURRENT_DATE),
       (3, 'Savings Plan', 5000.0, 'USD', 'Savings', 'ACTIVE', '2024-01-01', '2024-12-31', CURRENT_DATE, CURRENT_DATE),
       (4, 'Groceries Budget', 300.0, 'USD', 'Groceries', 'ACTIVE', '2024-06-01', '2024-06-30', CURRENT_DATE,
        CURRENT_DATE),
       (5, 'Entertainment Budget', 200.0, 'USD', 'Entertainment', 'ACTIVE', '2024-06-01', '2024-06-30', CURRENT_DATE,
        CURRENT_DATE);

INSERT INTO budget_wallet (budget_id, wallet_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (2, 1), -- Vacation Budget also linked to John's Personal Wallet
       (3, 1); -- Savings Plan also linked to John's Personal Wallet
