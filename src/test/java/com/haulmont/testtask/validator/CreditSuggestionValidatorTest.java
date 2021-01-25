package com.haulmont.testtask.validator;

import com.haulmont.testtask.domain.Client;
import com.haulmont.testtask.domain.Credit;
import com.haulmont.testtask.domain.CreditSuggestion;
import com.haulmont.testtask.validator.impls.CreditSuggestionValidator;
import org.junit.jupiter.api.*;

import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreditSuggestionValidatorTest {
    private static CreditSuggestionValidator validator;
    private static CreditSuggestion suggestion;
    private static Client client;
    private static Credit credit;

    @BeforeAll
    static void initValidator() {
        validator = CreditSuggestionValidator.getInstance();
    }

    @BeforeAll
    static void initClient() {
        client = new Client();
        client.setId(UUID.randomUUID());
        client.setFirstname("Олег");
        client.setLastname("Макаров");
        client.setPatronymic("Васильевич");
        client.setPhone("+7(056)692-58-85");
        client.setEmail("makarov.oleg@yandex.ru");
        client.setPassport("3608579420");
    }

    @BeforeEach
    void initCredit() {
        credit = new Credit();
        credit.setId(UUID.randomUUID());
        credit.setMinLimit("150000");
        credit.setMaxLimit("5000000");
        credit.setPercent("10");
    }

    @BeforeEach
    void initSuggestion() {
        suggestion = new CreditSuggestion();
        suggestion.setId(UUID.randomUUID());
        suggestion.setClient(client);
        suggestion.setCredit(credit);
        suggestion.setAmount("3000000");
    }

    @Test
    @Order(1)
    void isValid_AmountLowerThanMinLimit_ThrowValidatorException() {
        suggestion.setAmount("10000");
        Assertions.assertThrows(ValidatorException.class, () -> validator.isValid(suggestion));
    }

    @Test
    @Order(2)
    void isValid_AmountMoreThanMaxLimit_ThrowValidatorException() {
        suggestion.setAmount("8000000");
        Assertions.assertThrows(ValidatorException.class, () -> validator.isValid(suggestion));
    }

    @Test
    @Order(3)
    void isValid_IncorrectAmountFormat_ThrowValidatorException() {
        suggestion.setAmount("2000z00");
        Assertions.assertThrows(ValidatorException.class, () -> validator.isValid(suggestion));
    }

    @Test
    @Order(4)
    void isValid_IncorrectCreditBounds_ThrowValidatorException() {
        suggestion.getCredit().setMinLimit("200000");
        suggestion.getCredit().setMaxLimit("10000");
        suggestion.setAmount("100000");
        Assertions.assertThrows(ValidatorException.class, () -> validator.isValid(suggestion));
    }

    @Test
    @Order(5)
    void isValid_MinAndMaxLimitsEqualsAndAmountIsValid_TestPass() {
        suggestion.getCredit().setMinLimit("200000");
        suggestion.getCredit().setMaxLimit("200000");
        suggestion.setAmount("200000");
        Assertions.assertTrue(validator.isValid(suggestion));
    }

    @Test
    @Order(6)
    void isValid_CorrectFormat_TestPass() {
        suggestion.setAmount("2000000");
        Assertions.assertTrue(validator.isValid(suggestion));
    }
}
