package io.ermdev.ees.dao.exception;

public class SubjectNotBelongException extends RuntimeException {

    public SubjectNotBelongException(final String message) {
        super(message);
    }
}
