package io.ermdev.ees.legacy.dao.exception;

public class SubjectAlreadyPassedException extends RuntimeException {

    public SubjectAlreadyPassedException(final String message) {
        super(message);
    }
}
