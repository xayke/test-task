INSERT INTO BANK(id, name)
VALUES (UUID(), 'Сбербанк');
/* Да, это плохой подход к заполнению таких таблиц, но */
/* это чисто для удобства инициализации тестовой таблицы */
/* в этом проекте. */
INSERT INTO CLIENT(id, bank_id, firstname, lastname, patronymic, phone, email, passport)
VALUES (UUID(), (SELECT ID FROM BANK WHERE name = 'Сбербанк'),
        'Алексей', 'Застольный', 'Геннадьевич', '+7(956)246-88-00', 'zastolniy.a@mail.ru', '3719056845');
INSERT INTO CLIENT(id, bank_id, firstname, lastname, patronymic, phone, email, passport)
VALUES (UUID(), (SELECT ID FROM BANK WHERE name = 'Сбербанк'),
        'Владимир', 'Дятлов', 'Степанович', '+7(106)442-61-08', 'dyaltovvs@mail.ru', '3122716875');

INSERT INTO CREDIT(ID, BANK_ID, NAME, MIN_LIMIT, MAX_LIMIT, PERCENTS)
VALUES (UUID(), (SELECT ID FROM BANK WHERE name = 'Сбербанк'), 'На любые цели без подтверждения дохода',
        '30000', '300000', '12.9');
INSERT INTO CREDIT(ID, BANK_ID, NAME, MIN_LIMIT, MAX_LIMIT, PERCENTS)
VALUES (UUID(), (SELECT ID FROM BANK WHERE name = 'Сбербанк'), 'На любые цели',
        '30000', '300000', '9');

INSERT INTO CREDIT_SUGGESTION(ID, CLIENT_ID, CREDIT_ID, AMOUNT)
VALUES (UUID(),
        (SELECT ID FROM CLIENT WHERE FIRSTNAME = 'Алексей'),
        (SELECT ID FROM CREDIT WHERE PERCENTS = '9'),
        '150000');

INSERT INTO PAYMENT(ID, PAY_DATE, FULL_AMOUNT, PRINCIPAL_AMOUNT, INTEREST_AMOUNT, CREDIT_SUGG_ID)
VALUES (UUID(), '24-05-2021', '163500', '150000', '13500',
        (SELECT ID FROM CREDIT_SUGGESTION WHERE AMOUNT = '150000'));