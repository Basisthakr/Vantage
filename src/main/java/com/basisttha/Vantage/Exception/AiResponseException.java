package com.basisttha.Vantage.exception;

// Thrown when the AI provider returns a response that cannot be parsed into a FitAssessment. Mapped to HTTP 502 Bad Gateway by GlobalExceptionHandler
public class AiResponseException extends RuntimeException {

    public AiResponseException(String message) {
        super(message);
    }
}
