package com.ufund.api.ufundapi.service;

import com.ufund.api.ufundapi.model.APIResponse;
import java.io.IOException;

public interface GeminiAPIService {

    
    APIResponse generateGeminiResponse(String prompt) throws IOException;

}