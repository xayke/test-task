package com.haulmont.testtask.dao.impls;

import java.util.List;
import java.util.UUID;

public abstract class BankDependentDaoService<T> {
    public abstract T findById(UUID id);

    public abstract List<T> findAll();

    public abstract List<T> findAllWhereBankId(UUID id);

    public abstract void save(T data, UUID bankId);

    public abstract void update(T data, UUID bankId);

    public abstract void delete(T data);
}
