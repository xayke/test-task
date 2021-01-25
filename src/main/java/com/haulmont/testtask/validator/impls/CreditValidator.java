package com.haulmont.testtask.validator.impls;

import com.haulmont.testtask.domain.Credit;
import com.haulmont.testtask.validator.Validator;
import com.haulmont.testtask.validator.ValidatorException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreditValidator implements Validator<Credit> {
    private static CreditValidator INSTANCE;

    private static String namePattern = "^[A-ZА-ЯЁ]{1}[a-zа-яё ]{1,149}$";

    private List<ValidatorException> exceptions;

    private CreditValidator() {
    }

    public static CreditValidator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreditValidator();
        }
        return INSTANCE;
    }

    @Override
    public boolean isValid(Credit credit) throws ValidatorException {
        boolean result;
        exceptions = new ArrayList<>();
        result = (isNameValid(credit.getName())
                & isMinLimitValid(credit.getMinLimit())
                & isMaxLimitValid(credit.getMaxLimit())
                & isPercentsValid(credit.getPercent()))
                && isMinLowerThanMax(credit.getMinLimit(), credit.getMaxLimit());

        if (exceptions.size() != 0) {
            StringBuilder errors = new StringBuilder();
            for (ValidatorException exception : exceptions) {
                errors.append(exception.getMessage()).append('\n');
            }
            throw new ValidatorException(errors.toString());
        }
        return result;
    }

    private boolean isDecimal(String value) {
        boolean result;
        try {
            new BigDecimal(value);
            result = true;
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }

    private boolean isMatchesToPattern(String value, String pattern) {
        return value.matches(pattern);
    }

    private boolean isLengthValid(String value, int min, int max) {
        return (value.length() >= min) && (value.length() <= max);
    }

    private boolean isNameValid(String value) {
        boolean lengthValid = isLengthValid(value, 1, 150);
        boolean matchesPattern = isMatchesToPattern(value, namePattern);
        if (!lengthValid) {
            exceptions.add(new ValidatorException("Неправильная длинна наименования: требуется от 1 до 150"));
        }
        if (!matchesPattern) {
            exceptions.add(new ValidatorException("Наименование кредита не соответствует шаблону"));
        }

        return (lengthValid && matchesPattern);
    }

    private boolean isMinLowerThanMax(String min, String max) {
        boolean result;
        BigDecimal minValue = new BigDecimal(min);
        BigDecimal maxValue = new BigDecimal(max);
        if (maxValue.compareTo(minValue) >= 0) {
            result = true;
        } else {
            result = false;
            exceptions.add(new ValidatorException("Минимальное значение лимита больше чем максимальное"));
        }
        return result;
    }

    private boolean isMinLimitValid(String value) {
        boolean lengthValid = isLengthValid(value, 1, 40);
        boolean decimal = isDecimal(value);
        if (!lengthValid) {
            exceptions.add(new ValidatorException("Неправильная длинна минимального значения лимита: требуется от 1 до 40"));
        }
        if (!decimal) {
            exceptions.add(new ValidatorException("Минимальное значения лимита должно быть числом"));
        }
        return (lengthValid && decimal);
    }

    private boolean isMaxLimitValid(String value) {
        boolean lengthValid = isLengthValid(value, 1, 40);
        boolean decimal = isDecimal(value);
        if (!lengthValid) {
            exceptions.add(new ValidatorException("Неправильная длинна максимального значения лимита: требуется от 1 до 40"));
        }
        if (!decimal) {
            exceptions.add(new ValidatorException("Максимальное значения лимита должно быть числом"));
        }
        return (lengthValid && decimal);
    }

    private boolean isPercentsValid(String value) {
        boolean lengthValid = isLengthValid(value, 1, 10);
        boolean decimal = isDecimal(value);
        if (!lengthValid) {
            exceptions.add(new ValidatorException("Неправильная длинна процентов: требуется от 1 до 10"));
        }
        if (!decimal) {
            exceptions.add(new ValidatorException("Процентное значения должно быть числом и указываться через точку (если дробное)"));
        }
        return (lengthValid && decimal);
    }
}
