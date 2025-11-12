package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Donation model class
 */
public class APIResponseTest {
    private APIResponse apiResponse;

    /**
     * Initializes a new Donation object before each test
     */
    @BeforeEach
    public void setUp() {
        apiResponse = new APIResponse("AI stands for artificial intelligence.", "What does AI stand for?");
    }

    /**
     * Tests the Donation constructor
     */
    @Test
    public void testDonationConstructor() {
        assertEquals("AI stands for artificial intelligence.", apiResponse.getResponse());
        assertEquals("What does AI stand for?", apiResponse.getPrompt());
    }

    /**
     * Tests the getPrompt method
     */
    @Test
    public void testgetPrompt() {
        apiResponse.setPrompt("In a single sentence, what does AI stand for?");
        assertEquals("In a single sentence, what does AI stand for?", apiResponse.getPrompt());
    }

    /**
     * Tests the getResponse method
     */
    @Test
    public void testGetResponse() {
        apiResponse.setResponse("AI is an acronym for artificial intelligence.");
        assertEquals("AI is an acronym for artificial intelligence.", apiResponse.getResponse());
    }
}
