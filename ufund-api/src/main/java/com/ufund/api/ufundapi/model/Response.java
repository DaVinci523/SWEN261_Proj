package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
    
    private static String STRINGIFY = "Response [response=%s, prompt=%s]";

    @JsonProperty("response") private String response;
    @JsonProperty("prompt") private String prompt;

    public Response (@JsonProperty("response") String response, @JsonProperty("prompt") String prompt) {

        this.response = response;
        this.prompt = prompt;

    }

    public String getResponse(){
        return this.response;
    }

    public String getPrompt(){
        return this.prompt;
    }

    public void setResponse(String response){
        this.response = response;
    }

    public void setPrompt(String prompt){
        this.prompt = prompt;
    }

    public String toStrint(){
        return String.format(STRINGIFY, response, prompt);
    }

}
