package com.ufund.api.ufundapi.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;


/**
 * Gemini API to interact with the user
 * @author CTRL Team
 */
public class GeminiAPI {

    private Client client;


    /**
     * Creates the Gemini API
     * @param client Client to interact with Gemini API
     */
    public GeminiAPI(Client client) {
        this.client = Client.builder().apiKey("AIzaSyB3So1lECn4L2NRZlNhWhN7NAjmpEGlSPU").build();
    }
    
    /**
     * Generates a response from a given prompt
     * @param prompt user-provided prompt
     */
    public String generateResponse(String prompt) {

            // Call the Gemini API to generate the response
            GenerateContentResponse userResponse = client.models.generateContent("gemini-2.5-flash", prompt, null);
            
            // gets the text from the response and assigns it the response instance field
            return userResponse.text();
            
    }

    public String steve(){
        return "";
    }
}
