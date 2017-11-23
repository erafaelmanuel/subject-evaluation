package io.ermdev.ees.dao.exception;

public class SubjectDuplicateException extends RuntimeException {

    public SubjectDuplicateException(final String message) {
        super(message);
    }
}
