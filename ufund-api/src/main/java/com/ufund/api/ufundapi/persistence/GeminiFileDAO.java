package com.ufund.api.ufundapi.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.util.Map;
import java.util.TreeMap;
import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ufund.api.ufundapi.model.Response;

@Component
public class GeminiFileDAO implements GeminiDAO {

    private ObjectMapper objectMapper;
    private String filename;
    private Map<String, String> responses;


    public GeminiFileDAO(@Value("${responses.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    private boolean load() throws IOException {
        responses = new TreeMap<>();

        Response[] responsesArray = objectMapper.readValue(new File(filename), Response[].class);

        for (Response response : responsesArray){
            responses.put(response.getPrompt(), response.getResponse());
        }

        return true;
    }

    private boolean save() throws IOException{
        objectMapper.writeValue(new File(filename), responses);
        return true;
    }


    public String getResponse(String prompt){
        if(responses.containsKey(prompt)){
            return responses.get(prompt);
        }

    public String generateResponse
    }

}
