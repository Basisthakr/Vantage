package com.basisttha.Vantage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// @Size max is hardcoded to match vantage.max-text-length in application.properties to reduce costs
public record FitAssessmentRequest(

        @NotBlank(message = "Resume text must not be blank")
        @Size(max = 10000, message = "Resume text must not exceed 10000 characters")
        String resumeText,

        @NotBlank(message = "Job description text must not be blank")
        @Size(max = 10000, message = "Job description text must not exceed 10000 characters")
        String jobDescriptionText

) {}
