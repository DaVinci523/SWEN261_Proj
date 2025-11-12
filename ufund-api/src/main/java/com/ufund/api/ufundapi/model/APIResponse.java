package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Data class to serialize responses into JSON
 * 
 */
public class APIResponse {
    
    private static String STRINGIFY = "Response [response=%s, prompt=%s]";

    @JsonProperty("response") private String response;
    @JsonProperty("prompt") private String prompt;

    /**
     * Creates an APIResponse
     * @param response response generated form GeminiAPI
     * @param prompt prompt to generate response from
     *  
     */
    public APIResponse (@JsonProperty("response") String response, @JsonProperty("prompt") String prompt) {
        this.response = response;
        this.prompt = prompt;

    }

    /** 
     * gets the response
     * @return the response
    */
    public String getResponse(){
        return this.response;
    }

    /** 
     * gets the prompt
     * @return the prompt
    */
    public String getPrompt(){
        return this.prompt;
    }

    /**
     * sets a new response
     * @param response the new response
     */
    public void setResponse(String response){
        this.response = response;
    }

    /**
     * sets a new prompt
     * @param prompt the new prompt
     */
    public void setPrompt(String prompt){
        this.prompt = prompt;
    }

    /**
     * generates a string representaion of an APIResponse  
     * @return returns the string
     */
    @Override
    public String toString(){
        return String.format(STRINGIFY, response, prompt);
    }

}
