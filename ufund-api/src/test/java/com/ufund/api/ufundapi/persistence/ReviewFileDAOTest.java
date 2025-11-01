package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Review;

/**
 * Unit tests for the ReviewFileDAO class
 * @author Anthony Pelto
 */
@Tag("Persistence-tier")
public class ReviewFileDAOTest {
    private ReviewFileDAO reviewFileDAO;
    private ObjectMapper mockObjectMapper;
    private Review[] testReviews;

    /**
     * Setup the mock ObjectMapper and DAO before each of the tests
     */
    @BeforeEach
    public void setup() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);

        testReviews = new Review[] {
            new Review(1, "user1", "Great site", 5),
            new Review(2, "user2", "Kinda mid", 3),
            new Review(3, "user3", "Pretty Good", 4)
        };

        // Mock the readValue call to simulate the JSON
        when(mockObjectMapper.readValue(any(File.class), eq(Review[].class))).thenReturn(testReviews);

        // Create a dummy JSON file
        File dummyFile = File.createTempFile("reviews", ".json");
        dummyFile.deleteOnExit();
        try (java.io.FileWriter fw = new java.io.FileWriter(dummyFile)) {
            fw.write("[]");
        }

        // Construct the DAO using the mock object
        reviewFileDAO = new ReviewFileDAO(dummyFile.getAbsolutePath(), mockObjectMapper);
    }

    /**
     * Test getReviews returns all the reviews
     */
    @Test
    public void testGetReviews() {
        Review[] reviews = reviewFileDAO.getReviews();
        assertEquals(testReviews.length, reviews.length);
        for (int i = 0; i < testReviews.length; i++){
            assertEquals(testReviews[i], reviews[i]);
        }
    }

    /**
     * Test getReview() retrieves a review by ID
     */
    @Test
    public void testGetReview() throws IOException {
        Review review = reviewFileDAO.getReview(1);
        assertEquals(testReviews[0], review);
    }

    /**
     * Test getReview returns null when the review does not exist
     */
    @Test
    public void testGetReviewNotFound() throws IOException {
        Review review = reviewFileDAO.getReview(999);
        assertNull(review);
    }

    /**
     * Test createReview creates a new review with a unique ID
     */
    @Test
    public void testCreateReview() throws IOException {
        Review newReview = new Review(0, "newUser", "Worked great!", 5);
        Review result = reviewFileDAO.createReview(newReview);

        assertNotNull(result);
        assertNotEquals(0, result.getId());
        assertEquals("newUser", result.getUserName());
        assertEquals("Worked great!", result.getReviewText());
        assertEquals(5, result.getRating());

        //Verify that save worked
        verify(mockObjectMapper, atLeastOnce()).writeValue(any(File.class), any(Review[].class));
    }

    /**
     * Test that deleteReview removes an existing review
     */
    @Test
    public void testDeleteReview() throws IOException {
        // Delete a existing review
        boolean result = reviewFileDAO.deleteReview(1);
        assertTrue(result);

        // verify save() was called
        verify(mockObjectMapper, atLeastOnce()).writeValue(any(File.class), any(Review[].class));
    }

    /**
     * Test deleteReview() returns false if the id does not exist 
     */
    @Test
    public void testDeleteReviewNotFound() throws IOException {
        boolean result = reviewFileDAO.deleteReview(999);
        assertFalse(result);

        // Save should not be called when no review was deleted
        verify(mockObjectMapper, never()).writeValue(any(File.class), any(Review[].class));
    }

    /**
     * Test deleteAll removes all reviews and saves the updated file
     */
    @Test
    public void testDeleteAll() throws IOException {
        boolean result = reviewFileDAO.deleteAll();
        assertTrue(result);

        // Verify that save was called
        verify(mockObjectMapper, atLeastOnce()).writeValue(any(File.class), any(Review[].class));

        // Verify the reviews are empty
        assertEquals(0, reviewFileDAO.getReviews().length);
    }
}
