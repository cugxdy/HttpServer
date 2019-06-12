package org.cugxdy.exception;

public class ResourceNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 5563265404641097547L;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
