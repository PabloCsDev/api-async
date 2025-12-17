package com.hyus4ki.asyncpro.exception;
public class ProcessingException extends RuntimeException {
    private final String errorCode;
    public ProcessingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public String getErrorCode() {
        return errorCode;
    }
}