package com.haulmont.testtask.validator.impls;

import com.haulmont.testtask.domain.CreditSuggestion;
import com.haulmont.testtask.validator.Validator;
import com.haulmont.testtask.validator.ValidatorException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreditSuggestionValidator implements Validator<CreditSuggestion> {
    private static CreditSuggestionValidator INSTANCE;

    private List<ValidatorException> exceptions;

    private CreditSuggestionValidator() {
    }

    public static CreditSuggestionValidator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreditSuggestionValidator();
        }
        return INSTANCE;
    }

    @Override
    public boolean isValid(CreditSuggestion suggestion) {
        boolean result;
        exceptions = new ArrayList<>();
        result = isAmountValid(suggestion.getAmount())
                && isAmountAcceptable(suggestion.getAmount(), //amount
                suggestion.getCredit().getMinLimit(), suggestion.getCredit().getMaxLimit()); //min and max limits
        if (exceptions.size() != 0) {
            StringBuilder errors = new StringBuilder();
            for (ValidatorException exception : exceptions) {
                errors.append(exception.getMessage()).append('\n');
            }
            throw new ValidatorException(errors.toString());
        }
        return result;
    }

    private boolean isAmountAcceptable(String amount, String minLimit, String maxLimit) {
        boolean result = false;
        try {
            BigDecimal amountDecimal = new BigDecimal(amount);
            BigDecimal minLimitDecimal = new BigDecimal(minLimit);
            BigDecimal maxLimitDecimal = new BigDecimal(maxLimit);
            switch (minLimitDecimal.compareTo(maxLimitDecimal)) {
                case -1:
                    if ((amountDecimal.compareTo(minLimitDecimal) >= 0)
                            && (amountDecimal.compareTo(maxLimitDecimal) <= 0)) {
                        result = true;
                    } else {
                        result = false;
                        exceptions.add(new ValidatorException("Amount unacceptable: amount lower than min Limit or " +
                                "more than max limit"));
                    }
                    break;
                case 0:
                    if (amountDecimal.compareTo(minLimitDecimal) == 0) {
                        result = true;
                    } else {
                        result = false;
                        exceptions.add(new ValidatorException("Amount unacceptable: amount value is out of bound"));
                    }
                    break;
                case 1:
                    result = false;
                    exceptions.add(new ValidatorException("Bounds are incorrect: min limit is more than max limit"));
                    break;
            }
        } catch (NumberFormatException e) {
            result = false;
            exceptions.add(new ValidatorException("At least one of value is not decimal"));
        }
        return result;
    }

    private boolean isLengthValid(String value, int min, int max) {
        return (value.length() >= min) && (value.length() <= max);
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

    private boolean isAmountValid(String value) {
        boolean lengthValid = isLengthValid(value, 1, 40);
        boolean decimal = isDecimal(value);
        if (!lengthValid) {
            exceptions.add(new ValidatorException("Minimal limit's length isn't valid (must be between 1 and 40)"));
        }
        if (!decimal) {
            exceptions.add(new ValidatorException("Minimal limit's value is not decimal"));
        }
        return (isLengthValid(value, 1, 40) && isDecimal(value));
    }
}
