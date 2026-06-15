package com.basisttha.Vantage.service;

import com.basisttha.Vantage.exception.AiResponseException;
import com.basisttha.Vantage.exception.AiUnavailableException;
import com.basisttha.Vantage.model.FitAssessment;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FitAssessmentService {

    private final ChatClient chatClient;
    private final Resource promptResource;

    public FitAssessmentService(
            ChatClient.Builder chatClientBuilder,
            @Value("classpath:prompts/fit-assessment-prompt.st") Resource promptResource) {
        this.chatClient = chatClientBuilder.build();
        this.promptResource = promptResource;
    }

    public FitAssessment assess(String resumeText, String jobDescriptionText) {
        try {
            // PromptTemplate reads the .st file and substitutes {resume} and {jobDescription} placeholders
            PromptTemplate template = new PromptTemplate(promptResource);

            FitAssessment result = chatClient
                    .prompt(template.create(Map.of(
                            "resume", resumeText,
                            "jobDescription", jobDescriptionText
                    )))
                    .call()
                    .entity(FitAssessment.class);

            if (result == null) {
                throw new AiResponseException("The AI provider returned an empty response");
            }
            return result;

        } catch (AiResponseException | AiUnavailableException e) {
            throw e;
        } catch (Exception e) {
            if (isProviderError(e)) {
                throw new AiUnavailableException(
                        "AI provider is temporarily unavailable: " + e.getMessage());
            }
            throw new AiResponseException(
                    "Failed to parse AI response into a FitAssessment: " + e.getMessage());
        }
    }

    // Separates network failures or similar issues parse failures by using keywords
    private boolean isProviderError(Exception e) {
        String className = e.getClass().getName().toLowerCase();
        String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        return className.contains("timeout") || className.contains("connection")
                || message.contains("timeout") || message.contains("rate limit")
                || e instanceof java.io.IOException;
    }
}
