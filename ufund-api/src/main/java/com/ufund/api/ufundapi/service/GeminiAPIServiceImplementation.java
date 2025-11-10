package com.ufund.api.ufundapi.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.model.GeminiAPI;

/** 
 * Service latyer between the controller and API
 * @author CTRL Team
 */
@Service
public class GeminiAPIServiceImplementation implements GeminiAPIService {
    
    private final GeminiAPI geminiAPI;

    /**
     * Creates the GeiminiAPIService implementation
     * @param geminiAPI API to be linked to the 
     */
    @Autowired
    public GeminiAPIServiceImplementation(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    /**
    * Generates a response from the linked GeminiAPI
    * @param prompt String prompt to generate the response from
    * @return returns the response from the API   
    */
    public String generateGeminiResponse(String prompt) throws IOException {
        return geminiAPI.generateResponse(prompt);   
    }

}