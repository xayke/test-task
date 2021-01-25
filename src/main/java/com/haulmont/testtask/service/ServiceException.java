package com.haulmont.testtask.service;

public class ServiceException extends RuntimeException {
    public ServiceException(String message, Throwable e) {
        super(message, e);
    }
}
