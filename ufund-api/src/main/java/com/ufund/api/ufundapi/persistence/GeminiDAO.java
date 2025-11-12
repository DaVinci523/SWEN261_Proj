package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.APIResponse;

public interface GeminiDAO {

    APIResponse generateResponse(String prompt) throws IOException;

}