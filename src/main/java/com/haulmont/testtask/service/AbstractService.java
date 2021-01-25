package com.haulmont.testtask.service;

import com.haulmont.testtask.domain.fields.Field;

import java.util.List;
import java.util.UUID;

public abstract class AbstractService<T> {

    protected static final String FILTER_PATTERN = ".*%s.*";

    public AbstractService() {
    }

    public abstract List<T> getAll();

    public abstract T getById(UUID id);

    public abstract void save(T obj, UUID bankId);

    public abstract void delete(T obj);

    public abstract void  update(T obj, UUID bankId);

    public abstract List<T> searchByField(List<T> list, String input, Field field);
}
