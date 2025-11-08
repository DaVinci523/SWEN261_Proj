package com.ufund.api.ufundapi.service;

import java.io.IOException;

public interface GeminiAPIService {

    
    String generateGeminiResponse(String prompt) throws IOException;

}