package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Unit tests for GeminiAPI
 */
public class GeminiAPITest {

    private final static String PROMPT = "What is AI?";
    private GeminiAPI geminiAPI;

    @Mock
    private String response;

    /**
     * Intializes the mock objects before the tests
     */
    @BeforeEach
    public void setUp(){
        geminiAPI = new GeminiAPI();
    }

    /**
     * Tests the generateResponse method
     */
    @Test
    public void testGenerateResponse(){
        APIResponse result = geminiAPI.generateResponse(PROMPT);

        assertNotNull(result);

    }

}
