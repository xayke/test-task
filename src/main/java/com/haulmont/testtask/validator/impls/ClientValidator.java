package com.haulmont.testtask.validator.impls;

import com.haulmont.testtask.domain.Client;
import com.haulmont.testtask.validator.Validator;
import com.haulmont.testtask.validator.ValidatorException;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator implements Validator<Client> {
    private static ClientValidator INSTANCE;

    private static String phonePattern = "^[+]{1}[0-9]{1,3}[(]{1}[0-9]{3}[)]{1}[0-9]{3}[-]{1}[0-9]{2}[-]{1}[0-9]{2}$";
    private static String namePattern = "^[A-ZА-ЯЁ]{1}[a-zа-яё]{1,49}$";
    private static String emailPattern = "^[A-Za-z.0-9!#$%&'*+-/=?^_`{|}~]{2,64}[@]{1}[A-Za-z.0-9]{2,200}[.]{1}[a-z]{2,55}$";
    private static String passportPattern = "^[0-9]{10}$";

    private List<ValidatorException> exceptions;

    private ClientValidator() {
    }

    public static ClientValidator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientValidator();
        }
        return INSTANCE;
    }

    @Override
    public boolean isValid(Client client) throws ValidatorException {
        boolean result;
        exceptions = new ArrayList<>();
        result = isFirstnameValid(client.getFirstname())
                & isLastnameValid(client.getLastname())
                & isPatronymicValid(client.getPatronymic())
                & isPhoneValid(client.getPhone())
                & isEmailValid(client.getEmail())
                & isPassportValid(client.getPassport());
        if (exceptions.size() != 0) {
            StringBuilder errors = new StringBuilder();
            for (ValidatorException exception : exceptions) {
                errors.append(exception.getMessage()).append('\n');
            }
            throw new ValidatorException(errors.toString());
        }
        return result;
    }

    private boolean isLengthValid(String value, int min, int max) {
        return (value.length() >= min) && (value.length() <= max);
    }

    private boolean isMatchesToPattern(String value, String pattern) {
        return value.matches(pattern);
    }

    private boolean isFirstnameValid(String firstname) {
        boolean lengthValid = isLengthValid(firstname, 2, 50);
        boolean matchesPattern = isMatchesToPattern(firstname, namePattern);
        if (!lengthValid) {
            exceptions.add(new ValidatorException("Неправильная длинна имени: требуется от 2 до 50"));
        }
        if (!matchesPattern) {
            exceptions.add(new ValidatorException("Имя не соответствует шаблону"));
        }
        return lengthValid && matchesPattern;
    }

    private boolean isLastnameValid(String lastname) {
        boolean lengthValid = isLengthValid(lastname, 2, 50);
        boolean matchesPattern = isMatchesToPattern(lastname, namePattern);
        if (!lengthValid) {
            exceptions.add(new ValidatorException("Неправильная длинна фамилии: требуется от 2 до 50"));
        }
        if (!matchesPattern) {
            exceptions.add(new ValidatorException("Фамилия не соответствует шаблону"));
        }
        return lengthValid && matchesPattern;
    }

    private boolean isPatronymicValid(String patronymic) {
        if (patronymic == null) return true;
        boolean lengthValid = isLengthValid(patronymic, 2, 50);
        boolean matchesPattern = isMatchesToPattern(patronymic, namePattern);
        if (!lengthValid) {
            exceptions.add(new ValidatorException("Неправильная длинна отчества: требуется от 2 до 50"));
        }
        if (!matchesPattern) {
            exceptions.add(new ValidatorException("Отчество не соответствует шаблону"));
        }
        return lengthValid && matchesPattern;
    }

    private boolean isPhoneValid(String phone) {
        boolean lengthValid = isLengthValid(phone, 11, 20);
        boolean matchesPattern = isMatchesToPattern(phone, phonePattern);
        if (!lengthValid) {
            exceptions.add(new ValidatorException("Неправильная длинна телефона: требуется от 11 до 20"));
        }
        if (!matchesPattern) {
            exceptions.add(new ValidatorException("Номер телефона указывается в формате (с учетом спец символов): +7(000)000-00-00"));
        }
        return lengthValid && matchesPattern;
    }

    private boolean isEmailValid(String email) {
        if (email == null) return true;
        boolean lengthValid = isLengthValid(email, 7, 320);
        boolean matchesPattern = isMatchesToPattern(email, emailPattern);
        if (!lengthValid) {
            exceptions.add(new ValidatorException("Неправильная длинна email: требуется от 7 до 320"));
        }
        if (!matchesPattern) {
            exceptions.add(new ValidatorException("Email не соответствует шаблону"));
        }
        return lengthValid && matchesPattern;
    }

    private boolean isPassportValid(String passport) {
        boolean lengthValid = isLengthValid(passport, 10, 10);
        boolean matchesPattern = isMatchesToPattern(passport, passportPattern);
        if (!lengthValid) {
            exceptions.add(new ValidatorException("Неправильная длинна номера паспорта: требуется 10 (серия - 4 и номер 6)"));
        }
        if (!matchesPattern) {
            exceptions.add(new ValidatorException("Паспорт не соответствует шаблону"));
        }
        return lengthValid && matchesPattern;
    }
}
