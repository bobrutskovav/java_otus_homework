package ru.otus.war.core.service;

public class DbServiceException extends RuntimeException {
    public DbServiceException(Exception e) {
        super(e);
    }

    public DbServiceException(String message) {
        super(message);
    }
}
