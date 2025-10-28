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
    public void testSetRating(){
        // Setup
        Review review = new Review(1, "ajp", "Good website", 4);
        int expected_rating = 3;

        // Invoke
        review.setRating(expected_rating);

        // Analyze
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

}
