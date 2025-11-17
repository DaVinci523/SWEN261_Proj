package com.ufund.api.ufundapi.model;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.Chat;
import com.google.genai.Chats;
import com.google.genai.types.Content;
import com.google.genai.types.Part;


import org.springframework.stereotype.Component;

/**
 * Gemini API to interact with the user
 * @author CTRL Team
 */
@Component
public class GeminiAPI {

    private Client client;
    private Chat chat;

    private final String INIT_PROMPT = "";

    /**
     * Creates the Gemini API using GeminiAPIConfig to build the client  
     */
    public GeminiAPI() {
        this.client = GeminiAPIConfig.genaiClient(); 
        this.chat = this.client.chats.create("gemini-2.5-flash");
        this.initChat();
    }

    public void initChat(){

        GenerateContentResponse initResponse = this.chat.sendMessage(INIT_PROMPT);
    }
    /**
     * Generates a response from a given prompt
     * @param prompt user-provided prompt
     */
    public APIResponse generateResponse(String prompt) {

            // Call the Gemini API to generate the response
            GenerateContentResponse userResponse = this.chat.sendMessage(prompt);
            
            // gets the text from the response and assigns it the response instance field
            return new APIResponse(userResponse.text(),prompt);
            
    }
}
