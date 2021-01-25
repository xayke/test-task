package com.haulmont.testtask.dao;

public class DaoServiceException extends RuntimeException {

    public DaoServiceException(String message) {
        super(message);
    }

    public DaoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
