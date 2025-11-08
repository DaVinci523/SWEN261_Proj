package com.ufund.api.ufundapi.service;

import com.ufund.api.ufundapi.ai.GeminiAPI;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeminiAPIServiceImplementation {
    
    private final GeminiAPI geminiAPI;

    public GeminiAPIServiceImplementation(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    public String generateGeminiResponse(String prompt) {
        // Implementation to call Gemini API and return response
        return geminiAPI.generateResponse(prompt);   
    }

}