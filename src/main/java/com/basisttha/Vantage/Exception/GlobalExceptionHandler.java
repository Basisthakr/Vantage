package com.basisttha.Vantage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Bean Validation failure -- @NotBlank, @Size, etc. Collects all field errors so the caller knows everything that is wrong
    // in one shot, not just the first violation.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Validation failed", details));
    }

    // Model returned a response we couldnt deserialize to FitAssessment. 502 Bad Gateway: we got a response from the upstream (the AI provider)
    // but it was unusable.
    @ExceptionHandler(AiResponseException.class)
    public ResponseEntity<ErrorResponse> handleAiResponse(AiResponseException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponse(ex.getMessage(), null));
    }

    //Provider could not be reached, timed out, or hit a rate limit. 503 Service Unavailable: the upstream is down or throttling us.
    @ExceptionHandler(AiUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleAiUnavailable(AiUnavailableException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(ex.getMessage(), null));
    }
}