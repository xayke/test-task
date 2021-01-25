package com.haulmont.testtask.domain.fields.impls;

import com.haulmont.testtask.domain.fields.Field;

public enum CreditSuggestionField implements Field<CreditSuggestionField> {
    CLIENT,
    CREDIT,
    AMOUNT;

    CreditSuggestionField() {

    }


    @Override
    public CreditSuggestionField getValue() {
        return this;
    }
}
