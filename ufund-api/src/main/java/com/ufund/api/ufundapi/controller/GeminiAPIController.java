package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.GeminiAPI;
import com.ufund.api.ufundapi.service.GeminiAPIService;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Gemini API interactions
 * @author CTRL Team 
 */
@RestController
@RequestMapping("responses")
public class GeminiAPIController {
    private static final Logger LOG = Logger.getLogger(GeminiAPI.class.getName());
    private final GeminiAPIService geminiAPIService;

    /**
     * Constructor
     * @param geminiAPIService     
     */
    public GeminiAPIController(GeminiAPI geminiAPI, GeminiAPIService geminiAPIService ) {
        this.geminiAPIService = geminiAPIService;
    }

    /**
     * Gets the API's response for a given prompt
     * @param prompt user-provided prompt
     * @return ResponseEntity containing the response or error message, HttpStatus
     */
    @PostMapping("")
    public ResponseEntity<String> getGeminiResponse(String prompt) {
        LOG.info("POST /responses");
        try{
            System.out.println("PROMPT" + prompt);
            String response = geminiAPIService.generateGeminiResponse(prompt);
            return new ResponseEntity<String>(response, HttpStatus.OK);
        } 
        
        catch (IOException e) {
            LOG.severe("Error generating Gemini response: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }
    
}