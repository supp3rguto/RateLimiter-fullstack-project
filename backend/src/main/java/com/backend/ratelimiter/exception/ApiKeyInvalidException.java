package com.backend.ratelimiter.exception;

// esse .java é um erro para quando a api key não existe ou inválida
public class ApiKeyInvalidException extends RuntimeException {
    public ApiKeyInvalidException(String message) {
        super(message);
    }
}