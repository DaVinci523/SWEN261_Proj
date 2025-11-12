package com.ufund.api.ufundapi.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ufund.api.ufundapi.model.APIResponse;
import com.ufund.api.ufundapi.persistence.GeminiFileDAO;


public class GeminiAPIServiceTest {
     private GeminiAPIServiceImplementation geminiAPIServiceImplementation;
     private final static String PROMPT = "What is AI?";

    @Mock
    private GeminiFileDAO mockGeminiFileDAO;

    @Mock
    private APIResponse mockAPIResponse;

    /**
     * Initialize the mock objects before each test
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        geminiAPIServiceImplementation = new GeminiAPIServiceImplementation(mockGeminiFileDAO);
        // try {
        // mockAPIResponse = geminiAPIServiceImplementation.generateGeminiResponse(PROMPT);
        // }


    }

    /**
     * Tests the generateGeminiResponse method
     */
    @Test
    public void testgenerateGeminiResponse() throws IOException {
        when(mockGeminiFileDAO.generateResponse(PROMPT)).thenReturn(mockAPIResponse);
        APIResponse result = geminiAPIServiceImplementation.generateGeminiResponse(PROMPT);
        
        assertNotNull(result);
        verify(mockGeminiFileDAO, times(1)).generateResponse(PROMPT);
    }
}