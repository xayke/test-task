package com.haulmont.testtask.validator;

public interface Validator<T> {
    boolean isValid(T obj);
}
