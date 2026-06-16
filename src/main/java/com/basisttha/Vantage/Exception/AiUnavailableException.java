package com.basisttha.Vantage.exception;

// Thrown when the AI provider cannot be reached or signals a transient failure like
// timeout, rate limit, connection error. HTTP 503 Service Unavailable in GlobalExceptionHandler.
public class AiUnavailableException extends RuntimeException {

    public AiUnavailableException(String message) {
        super(message);
    }
}
