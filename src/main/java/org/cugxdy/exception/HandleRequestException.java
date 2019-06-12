package org.cugxdy.exception;

public class HandleRequestException extends RuntimeException{
    private static final long serialVersionUID = -630225144002649999L;

    public HandleRequestException() {
    }

    public HandleRequestException(String message) {
        super(message);
    }

    public HandleRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleRequestException(Throwable cause) {
        super(cause);
    }
}
