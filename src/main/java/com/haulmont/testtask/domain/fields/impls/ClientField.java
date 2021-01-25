package com.haulmont.testtask.domain.fields.impls;

import com.haulmont.testtask.domain.fields.Field;

public enum ClientField implements Field<ClientField> {
    FIRSTNAME,
    LASTNAME,
    PATRONYMIC,
    PHONE,
    EMAIL,
    PASSPORT;

    ClientField() {
    }

    @Override
    public ClientField getValue() {
        return this;
    }
}
