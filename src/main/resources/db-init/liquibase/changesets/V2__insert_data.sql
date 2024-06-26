INSERT INTO users (name, username, password, phone_number, date_of_birth, profile_image, gender, status, created_at,
                   updated_at)
VALUES ('John Doe', 'johndoe@gmail.com', '$2a$10$Xl0yhvzLIaJCDdKBS0Lld.ksK7c2Zytg/ZKFdtIYYQUv8rUfvCR4W', '123456789',
        '1990-01-05', null, 'MALE', 'ACTIVE', '2022-01-01 00:00:00', '2022-01-01 00:00:00'),

       ('Mike Smith', 'mikesmith@yahoo.com', '$2a$10$fFLij9aYgaNCFPTL9WcA/uoCRukxnwf.vOQ8nrEEOskrCNmGsxY7m',
        '987654321', '1995-02-10', null, 'MALE', 'ACTIVE', '2022-01-01 00:00:00', '2022-01-01 00:00:00'),

       ('Bob Brown', 'bobbrown@example.com', '$2a$10$SomeHashedPassword', '555555555',
        '1990-03-15', null, 'MALE', 'ACTIVE', '2022-01-01 00:00:00', '2022-01-01 00:00:00');

INSERT INTO users_roles (user_id, role)
VALUES (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER');

INSERT INTO wallet (user_id, name, balance, currency)
VALUES (1, 'John Wallet', 1000.00, 'USD'),
       (2, 'Mike Wallet', 5000.00, 'USD'),
       (3, 'Bob Wallet', 1500.00, 'USD');

INSERT INTO transaction (user_id, wallet_id, type, category, payment, amount, currency, note, date_created,
                         date_updated)
VALUES
    -- January
    (1, 1, 'EXPENSE', 'FOOD', 'CARD', 50.00, 'USD', 'Groceries', '2023-01-05 10:00:00', '2023-01-05 10:00:00'),
    (1, 1, 'EXPENSE', 'TRANSPORT', 'CARD', 20.00, 'USD', 'Bus Ticket', '2023-01-06 12:00:00', '2023-01-06 12:00:00'),
    (2, 2, 'INCOME', 'SALARY', 'BANK_TRANSFER', 3000.00, 'USD', 'January Salary', '2023-01-01 09:00:00',
     '2023-01-01 09:00:00'),
    (2, 2, 'EXPENSE', 'ENTERTAINMENT', 'CARD', 100.00, 'USD', 'Movie', '2023-01-07 15:00:00', '2023-01-07 15:00:00'),
    (3, 3, 'EXPENSE', 'FOOD', 'CARD', 60.00, 'USD', 'Dinner', '2023-01-08 18:00:00', '2023-01-08 18:00:00'),
    (3, 3, 'INCOME', 'BONUS', 'BANK_TRANSFER', 500.00, 'USD', 'Year-end Bonus', '2023-01-02 11:00:00',
     '2023-01-02 11:00:00'),
    (1, 1, 'EXPENSE', 'TRAVEL', 'CARD', 200.00, 'USD', 'Flight Ticket', '2023-01-10 20:00:00', '2023-01-10 20:00:00'),
    (2, 2, 'INCOME', 'FREELANCE', 'BANK_TRANSFER', 800.00, 'USD', 'Freelance Project', '2023-01-03 14:00:00',
     '2023-01-03 14:00:00'),
    (1, 1, 'EXPENSE', 'HEALTH', 'CARD', 75.00, 'USD', 'Doctor Visit', '2023-01-11 10:00:00', '2023-01-11 10:00:00'),
    (1, 1, 'EXPENSE', 'UTILITIES', 'BANK_TRANSFER', 150.00, 'USD', 'Electricity Bill', '2023-01-12 09:00:00',
     '2023-01-12 09:00:00'),
    (1, 1, 'EXPENSE', 'GROCERIES', 'CASH', 90.00, 'USD', 'Weekly Groceries', '2023-01-15 09:00:00',
     '2023-01-15 09:00:00'),

    -- February
    (2, 2, 'INCOME', 'INVESTMENT', 'BANK_TRANSFER', 1200.00, 'USD', 'Dividends', '2023-02-13 15:00:00',
     '2023-02-13 15:00:00'),
    (2, 2, 'EXPENSE', 'RENT', 'BANK_TRANSFER', 900.00, 'USD', 'Monthly Rent', '2023-02-14 10:00:00',
     '2023-02-14 10:00:00'),
    (3, 3, 'EXPENSE', 'EDUCATION', 'CARD', 250.00, 'USD', 'Online Course', '2023-02-15 16:00:00',
     '2023-02-15 16:00:00'),
    (3, 3, 'INCOME', 'GIFTS', 'BANK_TRANSFER', 200.00, 'USD', 'Birthday Gift', '2023-02-16 13:00:00',
     '2023-02-16 13:00:00'),
    (2, 2, 'EXPENSE', 'SHOPPING', 'CARD', 300.00, 'USD', 'New Clothes', '2023-02-17 14:00:00', '2023-02-17 14:00:00'),
    (2, 3, 'INCOME', 'RENT', 'BANK_TRANSFER', 1000.00, 'USD', 'Rental Income', '2023-02-18 12:00:00',
     '2023-02-18 12:00:00'),
    (1, 1, 'EXPENSE', 'FOOD', 'CARD', 30.00, 'USD', 'Restaurant', '2023-02-20 18:00:00', '2023-02-20 18:00:00'),
    (1, 1, 'EXPENSE', 'TRANSPORT', 'CARD', 25.00, 'USD', 'Taxi Ride', '2023-02-21 09:00:00', '2023-02-21 09:00:00'),
    (3, 3, 'INCOME', 'BONUS', 'BANK_TRANSFER', 600.00, 'USD', 'Performance Bonus', '2023-02-22 11:00:00',
     '2023-02-22 11:00:00'),
    (3, 3, 'EXPENSE', 'ENTERTAINMENT', 'CASH', 40.00, 'USD', 'Concert', '2023-02-25 20:00:00', '2023-02-25 20:00:00'),

    -- March
    (1, 1, 'EXPENSE', 'SUBSCRIPTION', 'CARD', 15.00, 'USD', 'Music Streaming', '2023-03-19 11:00:00',
     '2023-03-19 11:00:00'),
    (1, 1, 'EXPENSE', 'SHOPPING', 'CASH', 45.00, 'USD', 'Weekly Groceries', '2023-03-20 09:00:00',
     '2023-03-20 09:00:00'),
    (2, 2, 'INCOME', 'FREELANCE', 'BANK_TRANSFER', 300.00, 'USD', 'Freelance Work', '2023-03-21 10:00:00',
     '2023-03-21 10:00:00'),
    (2, 2, 'EXPENSE', 'ENTERTAINMENT', 'CARD', 60.00, 'USD', 'Concert Ticket', '2023-03-22 19:00:00',
     '2023-03-22 19:00:00'),
    (3, 3, 'EXPENSE', 'FOOD', 'CARD', 80.00, 'USD', 'Restaurant', '2023-03-23 18:00:00', '2023-03-23 18:00:00'),
    (3, 3, 'INCOME', 'INVESTMENT', 'BANK_TRANSFER', 1500.00, 'USD', 'Stock Sale', '2023-03-24 11:00:00',
     '2023-03-24 11:00:00'),
    (1, 1, 'EXPENSE', 'HEALTH', 'CARD', 50.00, 'USD', 'Pharmacy', '2023-03-25 12:00:00', '2023-03-25 12:00:00'),
    (2, 2, 'EXPENSE', 'UTILITIES', 'BANK_TRANSFER', 100.00, 'USD', 'Water Bill', '2023-03-26 09:00:00',
     '2023-03-26 09:00:00'),
    (3, 3, 'EXPENSE', 'GROCERIES', 'CARD', 70.00, 'USD', 'Weekly Groceries', '2023-03-27 18:00:00',
     '2023-03-27 18:00:00'),

    -- April
    (1, 1, 'EXPENSE', 'TRAVEL', 'CARD', 120.00, 'USD', 'Hotel Booking', '2023-04-25 20:00:00', '2023-04-25 20:00:00'),
    (1, 1, 'INCOME', 'FREELANCE', 'BANK_TRANSFER', 700.00, 'USD', 'Web Design', '2023-04-26 15:00:00',
     '2023-04-26 15:00:00'),
    (2, 2, 'EXPENSE', 'HEALTH', 'CARD', 50.00, 'USD', 'Medicine', '2023-04-27 10:00:00', '2023-04-27 10:00:00'),
    (3, 3, 'EXPENSE', 'TRANSPORT', 'CARD', 30.00, 'USD', 'Taxi', '2023-04-28 12:00:00', '2023-04-28 12:00:00'),
    (2, 2, 'INCOME', 'INVESTMENT', 'BANK_TRANSFER', 2000.00, 'USD', 'Dividends', '2023-04-15 10:00:00',
     '2023-04-15 10:00:00'),
    (2, 2, 'EXPENSE', 'RENT', 'BANK_TRANSFER', 800.00, 'USD', 'Monthly Rent', '2023-04-05 10:00:00',
     '2023-04-05 10:00:00'),
    (3, 3, 'INCOME', 'BONUS', 'BANK_TRANSFER', 700.00, 'USD', 'Year-end Bonus', '2023-04-02 11:00:00',
     '2023-04-02 11:00:00'),

    -- May
    (2, 2, 'INCOME', 'SALARY', 'BANK_TRANSFER', 3000.00, 'USD', 'May Salary', '2023-05-01 09:00:00',
     '2023-05-01 09:00:00'),
    (2, 2, 'EXPENSE', 'ENTERTAINMENT', 'CARD', 200.00, 'USD', 'Concert Tickets', '2023-05-05 19:00:00',
     '2023-05-05 19:00:00'),
    (3, 3, 'EXPENSE', 'FOOD', 'CARD', 70.00, 'USD', 'Groceries', '2023-05-08 18:00:00', '2023-05-08 18:00:00'),
    (3, 3, 'INCOME', 'BONUS', 'BANK_TRANSFER', 1000.00, 'USD', 'Performance Bonus', '2023-05-10 11:00:00',
     '2023-05-10 11:00:00'),
    (1, 1, 'EXPENSE', 'UTILITIES', 'BANK_TRANSFER', 120.00, 'USD', 'Gas Bill', '2023-05-12 09:00:00',
     '2023-05-12 09:00:00'),
    (1, 1, 'EXPENSE', 'HEALTH', 'CARD', 90.00, 'USD', 'Optician Visit', '2023-05-15 14:00:00', '2023-05-15 14:00:00'),
    (2, 2, 'INCOME', 'FREELANCE', 'BANK_TRANSFER', 600.00, 'USD', 'Website Project', '2023-05-18 10:00:00',
     '2023-05-18 10:00:00'),

    -- June
    (1, 1, 'EXPENSE', 'TRAVEL', 'CARD', 300.00, 'USD', 'Airline Ticket', '2023-06-10 20:00:00', '2023-06-10 20:00:00'),
    (3, 3, 'INCOME', 'FREELANCE', 'BANK_TRANSFER', 900.00, 'USD', 'Freelance Project', '2023-06-15 14:00:00',
     '2023-06-15 14:00:00'),
    (1, 1, 'EXPENSE', 'HEALTH', 'CARD', 100.00, 'USD', 'Dentist', '2023-06-20 10:00:00', '2023-06-20 10:00:00'),
    (1, 1, 'EXPENSE', 'UTILITIES', 'BANK_TRANSFER', 200.00, 'USD', 'Water Bill', '2023-06-25 09:00:00',
     '2023-06-25 09:00:00'),
    (2, 2, 'EXPENSE', 'RENT', 'BANK_TRANSFER', 850.00, 'USD', 'Monthly Rent', '2023-06-03 10:00:00',
     '2023-06-03 10:00:00'),
    (2, 2, 'INCOME', 'INVESTMENT', 'BANK_TRANSFER', 1100.00, 'USD', 'Interest', '2023-06-10 09:00:00',
     '2023-06-10 09:00:00'),

    -- July
    (2, 2, 'INCOME', 'INVESTMENT', 'BANK_TRANSFER', 1300.00, 'USD', 'Dividends', '2023-07-10 15:00:00',
     '2023-07-10 15:00:00'),
    (2, 2, 'EXPENSE', 'RENT', 'BANK_TRANSFER', 1000.00, 'USD', 'Monthly Rent', '2023-07-14 10:00:00',
     '2023-07-14 10:00:00'),
    (3, 3, 'EXPENSE', 'EDUCATION', 'CARD', 300.00, 'USD', 'Tuition Fees', '2023-07-18 16:00:00', '2023-07-18 16:00:00'),
    (3, 3, 'INCOME', 'GIFTS', 'BANK_TRANSFER', 250.00, 'USD', 'Anniversary Gift', '2023-07-20 13:00:00',
     '2023-07-20 13:00:00'),
    (1, 1, 'EXPENSE', 'UTILITIES', 'BANK_TRANSFER', 130.00, 'USD', 'Electricity Bill', '2023-07-05 09:00:00',
     '2023-07-05 09:00:00'),
    (1, 1, 'EXPENSE', 'FOOD', 'CARD', 45.00, 'USD', 'Weekly Groceries', '2023-07-10 11:00:00', '2023-07-10 11:00:00'),
    (2, 2, 'INCOME', 'FREELANCE', 'BANK_TRANSFER', 500.00, 'USD', 'Graphic Design', '2023-07-12 10:00:00',
     '2023-07-12 10:00:00'),

    -- August
    (1, 1, 'EXPENSE', 'SHOPPING', 'CARD', 400.00, 'USD', 'New Furniture', '2023-08-01 14:00:00', '2023-08-01 14:00:00'),
    (2, 2, 'INCOME', 'RENT', 'BANK_TRANSFER', 1200.00, 'USD', 'Rental Income', '2023-08-05 12:00:00',
     '2023-08-05 12:00:00'),
    (1, 1, 'EXPENSE', 'SUBSCRIPTION', 'CARD', 20.00, 'USD', 'Streaming Service', '2023-08-10 11:00:00',
     '2023-08-10 11:00:00'),
    (1, 1, 'EXPENSE', 'SHOPPING', 'CASH', 60.00, 'USD', 'Groceries', '2023-08-15 09:00:00', '2023-08-15 09:00:00'),
    (2, 2, 'EXPENSE', 'TRAVEL', 'CARD', 200.00, 'USD', 'Train Ticket', '2023-08-20 10:00:00', '2023-08-20 10:00:00'),
    (3, 3, 'INCOME', 'BONUS', 'BANK_TRANSFER', 700.00, 'USD', 'Performance Bonus', '2023-08-22 11:00:00',
     '2023-08-22 11:00:00'),

    -- September
    (2, 2, 'INCOME', 'FREELANCE', 'BANK_TRANSFER', 400.00, 'USD', 'Freelance Project', '2023-09-01 10:00:00',
     '2023-09-01 10:00:00'),
    (2, 2, 'EXPENSE', 'ENTERTAINMENT', 'CARD', 100.00, 'USD', 'Sports Event', '2023-09-05 19:00:00',
     '2023-09-05 19:00:00'),
    (3, 3, 'EXPENSE', 'FOOD', 'CARD', 90.00, 'USD', 'Restaurant', '2023-09-10 18:00:00', '2023-09-10 18:00:00'),
    (3, 3, 'INCOME', 'INVESTMENT', 'BANK_TRANSFER', 1600.00, 'USD', 'Stock Sale', '2023-09-15 11:00:00',
     '2023-09-15 11:00:00'),
    (1, 1, 'EXPENSE', 'UTILITIES', 'BANK_TRANSFER', 150.00, 'USD', 'Gas Bill', '2023-09-18 09:00:00',
     '2023-09-18 09:00:00'),
    (1, 1, 'EXPENSE', 'HEALTH', 'CARD', 75.00, 'USD', 'Therapy Session', '2023-09-20 10:00:00', '2023-09-20 10:00:00'),
    (2, 2, 'INCOME', 'FREELANCE', 'BANK_TRANSFER', 600.00, 'USD', 'Logo Design', '2023-09-22 10:00:00',
     '2023-09-22 10:00:00'),

    -- October
    (3, 3, 'EXPENSE', 'TRAVEL', 'CARD', 150.00, 'USD', 'Hotel Booking', '2023-10-20 20:00:00', '2023-10-20 20:00:00'),
    (3, 3, 'INCOME', 'FREELANCE', 'BANK_TRANSFER', 1000.00, 'USD', 'Freelance Project', '2023-10-25 14:00:00',
     '2023-10-25 14:00:00'),
    (1, 1, 'EXPENSE', 'HEALTH', 'CARD', 120.00, 'USD', 'Surgery', '2023-10-30 10:00:00', '2023-10-30 10:00:00'),
    (1, 1, 'EXPENSE', 'UTILITIES', 'BANK_TRANSFER', 250.00, 'USD', 'Electricity Bill', '2023-10-31 09:00:00',
     '2023-10-31 09:00:00'),
    (2, 2, 'INCOME', 'INVESTMENT', 'BANK_TRANSFER', 1300.00, 'USD', 'Dividends', '2023-10-15 09:00:00',
     '2023-10-15 09:00:00'),
    (2, 2, 'EXPENSE', 'RENT', 'BANK_TRANSFER', 950.00, 'USD', 'Monthly Rent', '2023-10-05 10:00:00',
     '2023-10-05 10:00:00'),

    -- November
    (2, 2, 'INCOME', 'INVESTMENT', 'BANK_TRANSFER', 1400.00, 'USD', 'Dividends', '2023-11-10 15:00:00',
     '2023-11-10 15:00:00'),
    (2, 2, 'EXPENSE', 'RENT', 'BANK_TRANSFER', 1100.00, 'USD', 'Monthly Rent', '2023-11-14 10:00:00',
     '2023-11-14 10:00:00'),
    (3, 3, 'EXPENSE', 'EDUCATION', 'CARD', 350.00, 'USD', 'Seminar Fees', '2023-11-18 16:00:00', '2023-11-18 16:00:00'),
    (3, 3, 'INCOME', 'GIFTS', 'BANK_TRANSFER', 300.00, 'USD', 'Wedding Gift', '2023-11-20 13:00:00',
     '2023-11-20 13:00:00'),
    (1, 1, 'EXPENSE', 'HEALTH', 'CARD', 80.00, 'USD', 'Chiropractor', '2023-11-23 11:00:00', '2023-11-23 11:00:00'),
    (1, 1, 'EXPENSE', 'SHOPPING', 'CASH', 100.00, 'USD', 'Black Friday Shopping', '2023-11-25 12:00:00',
     '2023-11-25 12:00:00'),
    (2, 2, 'INCOME', 'FREELANCE', 'BANK_TRANSFER', 700.00, 'USD', 'Mobile App Development', '2023-11-28 10:00:00',
     '2023-11-28 10:00:00'),

    -- December
    (1, 1, 'EXPENSE', 'SHOPPING', 'CARD', 500.00, 'USD', 'Christmas Gifts', '2023-12-01 14:00:00',
     '2023-12-01 14:00:00'),
    (2, 2, 'INCOME', 'RENT', 'BANK_TRANSFER', 1300.00, 'USD', 'Rental Income', '2023-12-05 12:00:00',
     '2023-12-05 12:00:00'),
    (1, 1, 'EXPENSE', 'SUBSCRIPTION', 'CARD', 25.00, 'USD', 'Magazine Subscription', '2023-12-10 11:00:00',
     '2023-12-10 11:00:00'),
    (1, 1, 'EXPENSE', 'SHOPPING', 'CASH', 75.00, 'USD', 'Weekly Groceries', '2023-12-15 09:00:00',
     '2023-12-15 09:00:00'),
    (3, 3, 'EXPENSE', 'TRAVEL', 'CARD', 400.00, 'USD', 'Holiday Trip', '2023-12-20 18:00:00', '2023-12-20 18:00:00'),
    (3, 3, 'INCOME', 'BONUS', 'BANK_TRANSFER', 1500.00, 'USD', 'Year-end Bonus', '2023-12-22 11:00:00',
     '2023-12-22 11:00:00'),
    (2, 2, 'EXPENSE', 'HEALTH', 'CARD', 90.00, 'USD', 'Physiotherapy', '2023-12-25 14:00:00', '2023-12-25 14:00:00'),
    (2, 2, 'INCOME', 'FREELANCE', 'BANK_TRANSFER', 800.00, 'USD', 'Website Development', '2023-12-28 10:00:00',
     '2023-12-28 10:00:00');


INSERT INTO budget (user_id, name, budgeted_amount, currency, category, status, start_date, end_date,
                    creation_date, last_update_date)
VALUES (1, 'Food Budget', 500.00, 'USD', 'FOOD', 'ACTIVE', '2023-01-01', '2023-12-31', '2023-01-01', '2023-01-01'),
       (2, 'Travel Budget', 2000.00, 'USD', 'TRAVEL', 'ACTIVE', '2023-01-01', '2023-12-31', '2023-01-01',
        '2023-01-01'),
       (3, 'Education Budget', 1000.00, 'USD', 'EDUCATION', 'ACTIVE', '2023-01-01', '2023-12-31', '2023-01-01',
        '2023-01-01');

INSERT INTO budget_wallet (budget_id, wallet_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);
