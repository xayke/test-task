package com.haulmont.testtask.domain.fields.impls;

import com.haulmont.testtask.domain.fields.Field;

public enum CreditField implements Field<CreditField> {
    NAME,
    MIN_LIMIT,
    MAX_LIMIT,
    PERCENT;

    CreditField() {
    }

    @Override
    public CreditField getValue() {
        return this;
    }
}
