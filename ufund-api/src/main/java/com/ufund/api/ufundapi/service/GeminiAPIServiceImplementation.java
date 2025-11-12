package com.ufund.api.ufundapi.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.model.APIResponse;
import com.ufund.api.ufundapi.persistence.GeminiFileDAO;

/** 
 * Service latyer between the controller and API
 * @author CTRL Team
 */
@Service
public class GeminiAPIServiceImplementation implements GeminiAPIService {
    
    private final GeminiFileDAO geminiFileDAO;

    /**
     * Creates the GeiminiAPIService implementation
     * @param geminiAPI API to be linked to the 
     */
    @Autowired
    public GeminiAPIServiceImplementation(GeminiFileDAO geminiFileDAO) {
        this.geminiFileDAO = geminiFileDAO;
    }

    /**
    * Generates a response from the linked GeminiAPI
    * @param prompt String prompt to generate the response from
    * @return returns the response from the API   
    */
    public APIResponse generateGeminiResponse(String prompt) throws IOException {
        return geminiFileDAO.generateResponse(prompt);   

    }
}