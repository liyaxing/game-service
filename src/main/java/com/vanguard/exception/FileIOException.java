package com.vanguard.exception;

public class FileIOException extends RuntimeException {

    public FileIOException() {
        super();
    }

    public FileIOException(String message) {
        super(message);
    }
}
