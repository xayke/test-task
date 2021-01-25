package com.haulmont.testtask.domain.fields.impls;

import com.haulmont.testtask.domain.fields.Field;

public enum BankField implements Field<BankField> {
    NAME;

    @Override
    public BankField getValue() {
        return this;
    }
}
