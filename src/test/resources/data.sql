DELETE FROM budget_wallet;
DELETE FROM budget;
DELETE FROM transaction;
DELETE FROM wallet_member;
DELETE FROM wallet;
DELETE FROM users_roles;
DELETE FROM users;

INSERT INTO users (name, username, password, phone_number, date_of_birth, profile_image, gender)
VALUES
    ('adminName', 'admin@gmail.com', 'password', '1234567890', '1990-01-01', 'profile.jpg', 'MALE'),
    ('userName1', 'user1@gmail.com', 'password', '0987654321', '1992-02-02', 'profile2.jpg', 'FEMALE'),
    ('userName2', 'user2@gmail.com', 'password', '0987654321', '1992-02-02', 'profile3.jpg', 'MALE');

INSERT INTO users_roles (user_id, role)
VALUES
    ((SELECT id FROM users WHERE username = 'admin@gmail.com'), 'ROLE_ADMIN'),
    ((SELECT id FROM users WHERE username = 'admin@gmail.com'), 'ROLE_USER'),
    ((SELECT id FROM users WHERE username = 'user1@gmail.com'), 'ROLE_USER'),
    ((SELECT id FROM users WHERE username = 'user2@gmail.com'), 'ROLE_USER');

INSERT INTO wallet (user_id, name, balance, currency)
VALUES
    ((SELECT id FROM users WHERE username = 'admin@gmail.com'), 'Admin Wallet', 1000.0, 'USD'),
    ((SELECT id FROM users WHERE username = 'user1@gmail.com'), 'User Wallet', 2000.0, 'USD');

INSERT INTO wallet_member (user_id, wallet_id)
VALUES
    ((SELECT id FROM users WHERE username = 'user2@gmail.com'), (SELECT id FROM wallet WHERE name = 'User Wallet'));

INSERT INTO transaction (user_id, wallet_id, type, category, payment, amount, currency, note, date_created, date_updated)
VALUES
    ((SELECT id FROM users WHERE username = 'admin@gmail.com'), (SELECT id FROM wallet WHERE name = 'Admin Wallet'), 'EXPENSE', 'GROCERIES', 'CARD', 50.0, 'USD', 'Grocery shopping', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'user1@gmail.com'), (SELECT id FROM wallet WHERE name = 'User Wallet'), 'INCOME', 'SALARY', 'BANK_TRANSFER', 1500.0, 'USD', 'Monthly salary', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO budget (user_id, name, budgeted_amount, currency, category, status, start_date, end_date, creation_date, last_update_date)
VALUES
    ((SELECT id FROM users WHERE username = 'admin@gmail.com'), 'Monthly Budget', 1000.0, 'USD', 'SHOPPING', 'ACTIVE', '2024-06-01', '2024-06-30', CURRENT_DATE, CURRENT_DATE),
    ((SELECT id FROM users WHERE username = 'user1@gmail.com'), 'Vacation Budget', 2000.0, 'USD', 'TRAVEL', 'ACTIVE', '2024-07-01', '2024-07-31', CURRENT_DATE, CURRENT_DATE);

INSERT INTO budget_wallet (budget_id, wallet_id)
VALUES
    ((SELECT id FROM budget WHERE name = 'Monthly Budget'), (SELECT id FROM wallet WHERE name = 'Admin Wallet')),
    ((SELECT id FROM budget WHERE name = 'Vacation Budget'), (SELECT id FROM wallet WHERE name = 'User Wallet'));
