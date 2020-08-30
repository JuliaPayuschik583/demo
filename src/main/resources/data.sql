DROP TABLE IF EXISTS billionaires;
DROP TABLE IF EXISTS participants;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS transactions;

CREATE TABLE participants
(
    participant_id int auto_increment primary key,
    name           varchar(255) not null
);

CREATE TABLE accounts
(
    account_id int auto_increment primary key,
    participant_id int not null,
    amount bigint not null,
    currency varchar(255) not null
    --INDEX (participant_id)
);

CREATE INDEX participant_id ON accounts(participant_id);

CREATE TABLE currencies
(
    from_currency varchar(255) not null,
    to_currency varchar(255) not null,
    factor int not null
);

create table transactions
(
    transaction_id int auto_increment primary key,
    from_participant_id int not null,
    to_participant_id int not null,
    from_account_id int not null,
    to_account_id int not null,
    date int not null,
    status int not null comment '0-start(created), 1-successful, 2-error',
    message varchar(255)
);

create table operations
(
    operation_id int auto_increment primary key,
    transaction_id int not null,
    account_id int not null,
    type int not null comment '1+add(plus) 0-subtract(minus)',
    date int not null,
    amount int not null
);

CREATE INDEX date ON operations(date);

INSERT INTO participants (name) VALUES
('Bill'),
('Tom');

INSERT INTO accounts (participant_id, amount, currency) VALUES
  (1, 1000, 'UAH'),
  (1, 55, 'UAH'),
  (2, 500, 'USD'),
  (1, 100, 'USD');

INSERT INTO currencies (from_currency, to_currency, factor) VALUES
('UAH', 'USD', 10),
('USD', 'UAH', 100);

--INSERT INTO transactions (from_participant_id, to_participant_id, from_account_id, to_account_id, date, status) VALUES (1,1, 1,1,  123, 0)