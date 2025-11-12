package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.APIResponse;

public interface GeminiDAO {
 
    String getResponse(String prompt);

    APIResponse generateResponse(String prompt) throws IOException;

}