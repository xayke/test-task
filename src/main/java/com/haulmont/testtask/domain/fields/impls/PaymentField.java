package com.haulmont.testtask.domain.fields.impls;

import com.haulmont.testtask.domain.fields.Field;

public enum PaymentField implements Field<PaymentField> {
    DATE,
    FULL_AMOUNT,
    PRINCIPAL_AMOUNT,
    INTEREST_AMOUNT;

    @Override
    public PaymentField getValue() {
        return this;
    }
}
