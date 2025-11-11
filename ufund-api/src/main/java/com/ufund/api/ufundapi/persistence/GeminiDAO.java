package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Response;

public interface GeminiDAO {
 
    String getResponse(String prompt);

    Response generateResponse(String prompt) throws IOException;

}