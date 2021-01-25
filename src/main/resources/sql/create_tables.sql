CREATE TABLE IF NOT EXISTS bank
(
    id   UUID         NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS client
(
    id         UUID        NOT NULL PRIMARY KEY,
    bank_id    UUID        NOT NULL,
    firstname  VARCHAR(50) NOT NULL,
    lastname   VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    phone      VARCHAR(20) NOT NULL,
    email      VARCHAR(320),
    passport   VARCHAR(10) NOT NULL,
    FOREIGN KEY (bank_id) REFERENCES bank (id)
);
CREATE TABLE IF NOT EXISTS credit
(
    id        UUID         NOT NULL PRIMARY KEY,
    bank_id   UUID         NOT NULL,
    name      VARCHAR(150) NOT NULL,
    min_limit VARCHAR(40)  NOT NULL,
    max_limit VARCHAR(40)  NOT NULL,
    percents  VARCHAR(10)  NOT NULL,
    FOREIGN KEY (bank_id) REFERENCES bank (id)
);
CREATE TABLE IF NOT EXISTS credit_suggestion
(
    id        UUID        NOT NULL PRIMARY KEY,
    client_id UUID        NOT NULL,
    credit_id UUID        NOT NULL,
    amount    VARCHAR(40) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client (id),
    FOREIGN KEY (credit_id) REFERENCES credit (id)
);
CREATE TABLE IF NOT EXISTS payment
(
    id               UUID        NOT NULL PRIMARY KEY,
    pay_date         VARCHAR(60) NOT NULL,
    full_amount      VARCHAR(40) NOT NULL,
    principal_amount VARCHAR(40) NOT NULL,
    interest_amount  VARCHAR(40) NOT NULL,
    credit_sugg_id   UUID        NOT NULL,
    FOREIGN KEY (credit_sugg_id) REFERENCES credit_suggestion (id)
);