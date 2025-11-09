package com.ufund.api.ufundapi.service;

import com.ufund.api.ufundapi.ai.GeminiAPI;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 
 * Service latyer between the controller and API
 */
@Service
public class GeminiAPIServiceImplementation {
    
    private final GeminiAPI geminiAPI;

    @Autowired
    public GeminiAPIServiceImplementation(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    public String generateGeminiResponse(String prompt) throws IOException {
        return geminiAPI.generateResponse(prompt);   
    }

}