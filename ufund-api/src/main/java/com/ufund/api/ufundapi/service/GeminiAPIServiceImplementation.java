package com.ufund.api.ufundapi.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Configuration;

import com.ufund.api.ufundapi.model.GeminiAPI;

/** 
 * Service latyer between the controller and API
 */
@Service
public class GeminiAPIServiceImplementation implements GeminiAPIService {
    
    private final GeminiAPI geminiAPI;

    @Autowired
    public GeminiAPIServiceImplementation(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    public String generateGeminiResponse(String prompt) throws IOException {
        return geminiAPI.generateResponse(prompt);   
    }

}