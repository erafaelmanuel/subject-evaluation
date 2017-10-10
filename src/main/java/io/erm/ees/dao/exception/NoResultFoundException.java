package io.erm.ees.dao.exception;

public class NoResultFoundException extends RuntimeException {

    public NoResultFoundException(final String message) {
        super(message);
    }
}
