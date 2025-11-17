package com.ufund.api.ufundapi.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ufund.api.ufundapi.model.APIResponse;
import com.ufund.api.ufundapi.model.GeminiAPI;


/**
 * Implements the functionality for JSON file-based peristance for APIResponses
 * @author CTRL Team 
 */
@Component
public class GeminiFileDAO implements GeminiDAO {

    private ObjectMapper objectMapper;
    private String filename;
    private List<APIResponse> responses;
    private GeminiAPI geminiAPI;


    /**
     * Creates Gemini DAO (Data Access Object)
     * @param filename Filename to rw from
     * @param objectMapper provides deserialization & serialization for JSON
     */
    public GeminiFileDAO(@Value("${responses.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load(); // loads in responses from file
    }

    /**
     * loads in responses from file
     * @return returns true
     */
    private boolean load() throws IOException {
        responses = new ArrayList<>();

        APIResponse[] responsesArray = objectMapper.readValue(new File(filename), APIResponse[].class);

        if(responsesArray == null){
            responsesArray = new APIResponse[0];
        } else {

            for (APIResponse response : responsesArray){
                responses.add(response);
            }
        }
        return true;
    }

    /**
     * saves responses to file
     * @return returns true
     */
    private boolean save() throws IOException{
        objectMapper.writeValue(new File(filename), responses);
        return true;
    }

    /**
     * Calls the API to generate a response from the prompt 
     * @param prompt prompt to generate the response with
     * @return returns an APIResponse instance containing the prompt and its response  
     */
    public APIResponse generateResponse(String prompt) throws IOException{
            
        // generates the response
        APIResponse messyResponse = geminiAPI.generateResponse(prompt);
        
        // cleans the response of special characters and creates new APIResponse
        String messyResp = messyResponse.getResponse();
        String cleanResp = messyResp.replaceAll("[\\\\-\\\\+\\\\\\^\"]", "");
        APIResponse cleanResponse = new APIResponse(cleanResp, prompt);
        
        // adds the clean response to responses
        responses.add(cleanResponse);
        
        // saves the new response to the file
        save();
        
        return cleanResponse;
            
    }

}
