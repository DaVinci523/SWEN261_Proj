package com.ufund.api.ufundapi.service;

import com.ufund.api.ufundapi.model.APIResponse;
import java.io.IOException;

/**
 * interface for GeminiAPIService
 * @author CTRLTeam 
 */
public interface GeminiAPIService {

    
    APIResponse generateGeminiResponse(String prompt) throws IOException;

}