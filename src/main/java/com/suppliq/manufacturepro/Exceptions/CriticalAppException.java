package com.suppliq.manufacturepro.Exceptions;


public class CriticalAppException extends RuntimeException {
    public CriticalAppException(String message) {
        super(message);
    }

    public CriticalAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
