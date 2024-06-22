INSERT INTO users (name, username, password, phone_number, date_of_birth, profile_image, gender)
VALUES ('adminName', 'admin@gmail.com', 'password',
        '1234567890', '1990-01-01', 'profile.jpg', 'MALE'),
       ('userName1', 'user1@gmail.com', 'password',
        '0987654321', '1992-02-02', 'profile2.jpg', 'FEMALE'),
       ('userName2', 'user2@gmail.com', 'password',
        '0987654321', '1992-02-02', 'profile3.jpg', 'MALE');

INSERT INTO users_roles (user_id, role)
VALUES (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER');

INSERT INTO wallet (user_id, name, balance, currency)
VALUES (1, 'Admin Wallet', 1000.0, 'USD'),
       (2, 'User Wallet', 2000.0, 'USD');

-- INSERT INTO wallet_member (user_id, wallet_id)
-- VALUES (1, 2); -- Admin linked to User Wallet

INSERT INTO transaction (user_id, wallet_id, type, category, payment, amount, currency, note, date_created, date_updated)
VALUES (1, 1, 'EXPENSE', 'Groceries', 'Credit Card', 50.0, 'USD', 'Grocery shopping', CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
       (2, 2, 'INCOME', 'Salary', 'Bank Transfer', 1500.0, 'USD', 'Monthly salary', CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO budget (user_id, name, budgeted_amount, currency, category, status, start_date, end_date, creation_date,
                    last_update_date)
VALUES (1, 'Monthly Budget', 1000.0, 'USD', 'General', 'ACTIVE', '2024-06-01', '2024-06-30', CURRENT_DATE,
        CURRENT_DATE),
       (2, 'Vacation Budget', 2000.0, 'USD', 'Travel', 'ACTIVE', '2024-07-01', '2024-07-31', CURRENT_DATE,
        CURRENT_DATE);

INSERT INTO budget_wallet (budget_id, wallet_id)
VALUES (1, 1),
       (2, 2);
--        (2, 1); -- Vacation Budget also linked to Admin's Wallet
