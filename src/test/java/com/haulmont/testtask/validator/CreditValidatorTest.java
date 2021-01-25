package com.haulmont.testtask.validator;

import com.haulmont.testtask.domain.Credit;
import com.haulmont.testtask.validator.impls.CreditValidator;
import org.junit.jupiter.api.*;

import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreditValidatorTest {

    private static CreditValidator validator;
    private static Credit credit;

    @BeforeAll
    static void sinitValidator() {
        validator = CreditValidator.getInstance();
    }

    @BeforeEach
    void initCredit() {
        credit = new Credit();
        credit.setId(UUID.randomUUID());
        credit.setMinLimit("150000");
        credit.setMaxLimit("5000000");
        credit.setPercent("10");
    }

    @Test
    @Order(1)
    void isValid_IncorrectMinLimit_ThrowValidatorException() {
        credit.setMinLimit("15a000");
        Assertions.assertThrows(ValidatorException.class, () -> validator.isValid(credit));
    }

    @Test
    @Order(2)
    void isValid_IncorrectMaxLimit_ThrowValidatorException() {
        credit.setMaxLimit("500b000");
        Assertions.assertThrows(ValidatorException.class, () -> validator.isValid(credit));
    }

    @Test
    @Order(3)
    void isValid_IncorrectPercent_ThrowValidatorException() {
        credit.setPercent("ten");
        Assertions.assertThrows(ValidatorException.class, () -> validator.isValid(credit));
    }

    @Test
    @Order(4)
    void isValid_CorrectCredit_TestPass() {
        Assertions.assertTrue(validator.isValid(credit));
    }
}
