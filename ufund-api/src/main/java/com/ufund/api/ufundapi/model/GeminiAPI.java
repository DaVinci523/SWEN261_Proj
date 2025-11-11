package com.ufund.api.ufundapi.model;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import org.springframework.stereotype.Component;

/**
 * Gemini API to interact with the user
 * @author CTRL Team
 */
@Component
public class GeminiAPI {

    private Client client;

    /**
     * Creates the Gemini API using GeminiAPIConfig to build the client  
     */
    public GeminiAPI() {
        this.client = GeminiAPIConfig.genaiClient();
    }
    
    /**
     * Generates a response from a given prompt
     * @param prompt user-provided prompt
     */
    public Response generateResponse(String prompt) {

            // Call the Gemini API to generate the response
            GenerateContentResponse userResponse = client.models.generateContent("gemini-2.5-flash-lite", prompt, null);
            
            // gets the text from the response and assigns it the response instance field
            return new Response(userResponse.text(),prompt);
            
    }
}
