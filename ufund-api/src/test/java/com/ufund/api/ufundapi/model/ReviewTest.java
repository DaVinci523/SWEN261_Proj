package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit tests for the Review class
 * @author Anthony Pelto
 */

 @Tag("Model-tier")
public class ReviewTest {
    @Test
    public void testConstructor(){
        //Setup
        int expected_id = 1;
        String expected_userName = "ajp";
        String expected_reviewText = "Best website ever!";
        int expected_rating = 5;

        // Invoke
        Review review = new Review(expected_id, expected_userName, expected_reviewText, expected_rating);

        //Analyze
        assertEquals(expected_id, review.getId());
        assertEquals(expected_userName, review.getUserName());
        assertEquals(expected_reviewText, review.getReviewText());
        assertEquals(expected_rating, review.getRating());
    }


    @Test
    public void testToString() {
        // Setup
        int id = 1;
        String userName = "ajp";
        String reviewText = "Good Website";
        int rating = 5;
        String expected_string = String.format(Review.STRINGIFY, id, userName, rating, reviewText);
        Review review = new Review(id, userName, reviewText, rating);

        // Invoke
        String actual_string = review.toString();

        // Analyze
        assertEquals(expected_string, actual_string);
    }

    @Test
    public void testGetters() {
        Review review = new Review(2, "userXman", "Nice site", 4);

        assertEquals(2, review.getId());
        assertEquals("userXman", review.getUserName());
        assertEquals("Nice site", review.getReviewText());
        assertEquals(4, review.getRating());
    }

    @Test
    public void testEdgeCases() {
        Review review = new Review(0, "", "", 0);

        assertEquals(0, review.getId());
        assertEquals("", review.getUserName());
        assertEquals("", review.getReviewText());
        assertEquals(0, review.getRating());

        String expectedString = String.format(Review.STRINGIFY, 0, "", 0, "");
        assertEquals(expectedString, review.toString());
    }

}
