package com.haulmont.testtask.validator;

import com.haulmont.testtask.domain.Client;
import com.haulmont.testtask.validator.impls.ClientValidator;
import org.junit.jupiter.api.*;

import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientValidatorTest {

    private static ClientValidator validator;
    private static Client client;

    @BeforeAll
    static void initValidator() {
        validator = ClientValidator.getInstance();
    }

    @BeforeEach
    void initClient() {
        client = new Client();
        client.setId(UUID.randomUUID());
        client.setFirstname("Олег");
        client.setLastname("Макаров");
        client.setPatronymic("Васильевич");
        client.setPhone("+7(056)692-58-85");
        client.setEmail("makarov.oleg@yandex.ru");
        client.setPassport("3608579420");
    }

    @Test
    @Order(1)
    void isValid_IncorrectFirstname_ThrowValidatorException() {
        client.setFirstname("олег"); // Lowercase. Expected: 1st letter uppercase
        Assertions.assertThrows(ValidatorException.class, () -> validator.isValid(client));
    }

    @Test
    @Order(2)
    void isValid_IncorrectLastname_ThrowValidatorException() {
        client.setLastname("В"); // Lowercase. Expected: 1st letter uppercase
        Assertions.assertThrows(ValidatorException.class, () -> validator.isValid(client));
    }

    @Test
    @Order(3)
    void isValid_NoPatronymic_TestPass() {
        client.setPatronymic(null);
        Assertions.assertTrue(validator.isValid(client));
    }

    @Test
    @Order(4)
    void isValid_IncorrectPhone_ThrowValidatorException() {
        client.setPhone("+79259620712");
        Assertions.assertThrows(ValidatorException.class, () -> validator.isValid(client));
    }

    @Test
    @Order(5)
    void isValid_NoEmail_TestPass() {
        client.setEmail(null);
        Assertions.assertTrue(validator.isValid(client));
    }

    @Test
    @Order(6)
    void isValid_IncorrectPassport_ThrowValidatorException() {
        client.setPassport("5212 076123");
        Assertions.assertThrows(ValidatorException.class, () -> validator.isValid(client));
    }

    @Test
    @Order(7)
    void isValid_ValidClient_TestPass() {
        Assertions.assertTrue(validator.isValid(client));
    }
}
