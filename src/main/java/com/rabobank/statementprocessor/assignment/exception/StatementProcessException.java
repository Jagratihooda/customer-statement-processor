package com.rabobank.statementprocessor.assignment.exception;

public class StatementProcessException extends RuntimeException {
    public StatementProcessException(String message) {
        super(message);
    }

    public StatementProcessException(Throwable throwable) {
        super(throwable);
    }

    public StatementProcessException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
