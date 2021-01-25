package com.haulmont.testtask.dao;

import java.util.List;
import java.util.UUID;

public interface DAOService<T> {
    T findById(UUID id);

    List<T> findAll();

    void save(T obj);

    void delete(T obj);

    void update(T obj);
}
