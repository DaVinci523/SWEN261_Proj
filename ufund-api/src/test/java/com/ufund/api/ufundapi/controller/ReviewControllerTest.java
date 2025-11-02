package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Review;
import com.ufund.api.ufundapi.service.ReviewService;

/**
 * Unit tests for ReviewController
 */
public class ReviewControllerTest {
    private ReviewController controller;
    private ReviewService mockService;

    @BeforeEach
    public void setup() {
        mockService = mock(ReviewService.class);
        controller = new ReviewController(mockService);
    }

    @Test
    public void testGetReviewsSuccess() throws IOException {
        Review[] reviews = { new Review(1, "A", "Great!", 5)};
        when(mockService.getReviews()).thenReturn(reviews);

        ResponseEntity<Review[]> response = controller.getReviews();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews, response.getBody());
    }

    @Test
    public void testGetReviewsNoContent() throws IOException {
        when(mockService.getReviews()).thenReturn(new Review[0]);
        ResponseEntity<Review[]> response = controller.getReviews();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetReviewsHandlesException() throws IOException {
        when(mockService.getReviews()).thenThrow(new IOException());

        ResponseEntity<Review[]> response = controller.getReviews();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateReviewSuccess() throws IOException {
        Review review = new Review(0, "B", "Good", 4);
        Review created = new Review(2, "B", "Good", 4);

        when(mockService.createReview(review)).thenReturn(created);
        ResponseEntity<Review> response = controller.createReview(review);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(created, response.getBody());
    }

    @Test
    public void testCreateReviewConflict() throws IOException {
        Review review = new Review(0, "B", "Good", 4);
        when(mockService.createReview(review)).thenReturn(null);

        ResponseEntity<Review> response = controller.createReview(review);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateReviewHandlesException() throws IOException {
        Review review = new Review(0, "B", "Good", 4);
        when(mockService.createReview(review)).thenThrow(new IOException());

        ResponseEntity<Review> response = controller.createReview(review);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteReviewSuccess() throws IOException {
        when(mockService.deleteReview(1)).thenReturn(true);

        ResponseEntity<Review> response = controller.deleteReview(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

     @Test
    public void testDeleteReviewNotFound() throws IOException {
        when(mockService.deleteReview(1)).thenReturn(false);

        ResponseEntity<Review> response = controller.deleteReview(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

     @Test
    public void testDeleteReviewHandlesException() throws IOException {
        when(mockService.deleteReview(1)).thenThrow(new IOException());

        ResponseEntity<Review> response = controller.deleteReview(1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
