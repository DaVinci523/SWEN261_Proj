package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.APIResponse;
import com.ufund.api.ufundapi.service.GeminiAPIServiceImplementation;

/**
 * Unit tests for the GeminiAPIController
 */
public class GeminiAPIControllerTest {
    private GeminiAPIController geminiAPIController;

    @Mock
    private GeminiAPIServiceImplementation mockGeminiAPIServiceImplementation;
    private APIResponse mockAPIResponse;

    /**
     * Initializes the mock objects before the tests
     */
    @BeforeEach 
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        geminiAPIController = new GeminiAPIController(mockGeminiAPIServiceImplementation);
    }

    /**
     * Tests the generateGeminiResponse method
     */
    @Test
    public void testgenerateGemResp() throws IOException {
        String mockPrompt = "What is AI?";
        when(mockGeminiAPIServiceImplementation.generateGeminiResponse(mockPrompt)).thenReturn(mockAPIResponse);
        ResponseEntity<APIResponse> result = geminiAPIController.generateGeminiResponse(mockPrompt);

        assertNotNull(result);
        verify(mockGeminiAPIServiceImplementation, times(1)).generateGeminiResponse(mockPrompt);
    }
}