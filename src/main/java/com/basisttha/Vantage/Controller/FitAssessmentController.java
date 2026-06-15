package com.basisttha.Vantage.controller;

import com.basisttha.Vantage.dto.FitAssessmentRequest;
import com.basisttha.Vantage.model.FitAssessment;
import com.basisttha.Vantage.service.FitAssessmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FitAssessmentController {

    private final FitAssessmentService fitAssessmentService;

    @PostMapping("/fit-assessment")
    public ResponseEntity<FitAssessment> assess(@Valid @RequestBody FitAssessmentRequest request) {
        FitAssessment assessment = fitAssessmentService.assess(
                request.resumeText(),
                request.jobDescriptionText()
        );
        return ResponseEntity.ok(assessment);
    }
}
