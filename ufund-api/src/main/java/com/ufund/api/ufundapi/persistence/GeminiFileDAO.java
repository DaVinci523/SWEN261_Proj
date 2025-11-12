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
     * 
     */
    public GeminiFileDAO(@Value("${responses.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.geminiAPI = new GeminiAPI();
        load();
    }

    private boolean load() throws IOException {
        responses = new ArrayList<>();

        APIResponse[] responsesArray = objectMapper.readValue(new File(filename), APIResponse[].class);

        for (APIResponse response : responsesArray){
            responses.add(response);
        }

        return true;
    }

    private boolean save() throws IOException{
        objectMapper.writeValue(new File(filename), responses);
        return true;
    }

    public APIResponse generateResponse(String prompt) throws IOException{
            
        APIResponse messyResponse = geminiAPI.generateResponse(prompt);
        
        String messyResp = messyResponse.getResponse();
        String cleanResp = messyResp.replaceAll("[\\\\-\\\\+\\\\.\\\\^:,\"*]", "");
        
        APIResponse cleanResponse = new APIResponse(cleanResp, prompt);
        
        responses.add(cleanResponse);
        
        save();
        
        return cleanResponse;
            
    }

}
