package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.APIResponse;

/**
 * Unit tests for the DonationFileDAO class
 */
public class GeminiFileDAOTest {
    private GeminiFileDAO geminiFileDAO;

    @Mock
    private ObjectMapper mockObjectMapper = new ObjectMapper();

    private final String testFilename = "test_response.json";

    /**
     * Initialized the DAO before each test
     */
    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        geminiFileDAO = new GeminiFileDAO(testFilename, mockObjectMapper);
    }

    /**
     * Tests the generateResponse method
     */
    @Test
    public void testgenerateResponse() throws IOException {
        String prompt = "What is AI?";
        APIResponse result = geminiFileDAO.generateResponse(prompt);
        assertNotNull(result);
    }

}