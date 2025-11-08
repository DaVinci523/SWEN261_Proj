package com.ufund.api.ufundapi.controller;

import com.ufund.api.ufundapi.ai.GeminiAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.ufund.api.ufundapi.service.GeminiAPIService;

import java.util.logging.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Gemini API interactions
 * @author CTRL Team 
 */
@RestController
@RequestMapping("/api/gemini")
public class GeminiAPIController {
    private static final Logger LOG = Logger.getLogger(GeminiAPI.class.getName()); 
    private final GeminiAPI geminiAPI;
    private final GeminiAPIService geminiAPIService;

    /**
     * Constructor
     * @param geminiApi 
     * @param geminiAPIService     
     */
    public GeminiAPIController(GeminiAPI geminiAPI, GeminiAPIService geminiAPIService ) {
        this.geminiAPI = geminiAPI;
        this.geminiAPIService = geminiAPIService;
    }

    /**
     * Generates the API's response to a given prompt
     * @param prompt user-provided prompt
     * @return response from the Gemini API
     */
    public String generateGeminiResponse(@RequestParam String prompt) {
        LOG.info("Received prompt: " + prompt);
        return geminiAPI.generateResponse(prompt);
    }

    /**
     * Gets the API's response for a given 
     * @param prompt user-provided prompt
     * @return ResponseEntity containing the response or error message, HttpStatus
     */
    public ResponseEntity<String> getGeminiResponse(String prompt) {
        try{
            String response = generateGeminiResponse(prompt);
            return new ResponseEntity<String>(response, HttpStatus.OK);
        } 
        catch (Exception e) {
            LOG.severe("Error generating Gemini response: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }
    
}