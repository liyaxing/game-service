package com.vanguard.exception;

public class FileValidationException extends RuntimeException {

    public FileValidationException() {
        super();
    }

    public FileValidationException(String message) {
        super(message);
    }
}
