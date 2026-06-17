package com.basisttha.Vantage.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

// @JsonInclude(NON_NULL) suppresses the details field in non-validation errors where it is null so response body stays clean
//  Eg - validation failure -> { "message": "Validation failed", "details": ["field: msg"] }
//   AI failure        -> { "message": "..." }   (no details key)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(String message, List<String> details) {}
