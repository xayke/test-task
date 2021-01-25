package com.haulmont.testtask.database;

public class DatabaseServiceException extends RuntimeException {
    public DatabaseServiceException(String message, Throwable ex) {
        super(message, ex);
    }
}
