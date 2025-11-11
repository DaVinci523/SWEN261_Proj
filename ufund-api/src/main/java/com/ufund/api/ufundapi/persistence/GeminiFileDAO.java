package com.ufund.api.ufundapi.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ufund.api.ufundapi.model.Response;
import com.ufund.api.ufundapi.model.GeminiAPI;

@Component
public class GeminiFileDAO implements GeminiDAO {

    private ObjectMapper objectMapper;
    private String filename;
    private List<Response> responses;
    private GeminiAPI geminiAPI;


    public GeminiFileDAO(@Value("${responses.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.geminiAPI = new GeminiAPI();
        load();
    }

    private boolean load() throws IOException {
        responses = new ArrayList<>();

        Response[] responsesArray = objectMapper.readValue(new File(filename), Response[].class);

        for (Response response : responsesArray){
            responses.add(response);
        }

        return true;
    }

    private boolean save() throws IOException{
        objectMapper.writeValue(new File(filename), responses);
        return true;
    }


    public String getResponse(String prompt){
            
            for(Response response : responses ){
                if(response.getPrompt() == prompt){
                    return response.getResponse();
                }
            }   
            return null;
    }

    public Response generateResponse(String prompt) throws IOException{
            Response newResponse = geminiAPI.generateResponse(prompt);
            responses.add(newResponse);
            save();
            return newResponse;
            
    }

}
