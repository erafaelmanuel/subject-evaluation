package io.ermdev.ees.dao.exception;

public class NoResultFoundException extends RuntimeException {

    public NoResultFoundException(final String message) {
        super(message);
    }
}
