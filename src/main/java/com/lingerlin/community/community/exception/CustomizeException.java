package com.lingerlin.community.community.exception;

/**
 * @author lingerlin
 */
public class CustomizeException extends RuntimeException {
    private String message;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     */
    public CustomizeException(ICustomizeErrorCode iCustomizeErrorCode) {
        this.message = iCustomizeErrorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
