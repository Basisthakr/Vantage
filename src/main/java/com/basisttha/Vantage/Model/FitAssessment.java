package com.basisttha.Vantage.model;

import java.util.List;

public record FitAssessment(
        int score,
        List<String> matchedSkills,
        List<String> missingSkills,
        String recommendation
) {}
