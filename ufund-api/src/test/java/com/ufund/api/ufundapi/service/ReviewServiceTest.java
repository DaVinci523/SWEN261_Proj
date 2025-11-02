package com.ufund.api.ufundapi.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Review;
import com.ufund.api.ufundapi.persistence.ReviewDAO;

/**
 * Unit tests for ReviewServiceImplementation
 */
public class ReviewServiceTest {
    private ReviewDAO mockDAO;
    private ReviewServiceImplementation service;

    @BeforeEach
    public void setup() {
        mockDAO = mock(ReviewDAO.class);
        service = new ReviewServiceImplementation(mockDAO);
    }

    @Test
    public void testGetReviews() throws IOException {
        Review[] expected = {
            new Review(1, "A", "Great", 5),
            new Review(2, "B", "Good", 4)
        };

        when(mockDAO.getReviews()).thenReturn(expected);
        Review[] actual = service.getReviews();

        assertArrayEquals(expected, actual);
        verify(mockDAO, times(1)).getReviews();
    }

    @Test
    public void testGetReviewsByRating() throws IOException {
        Review[] expected = { new Review(1, "A", "Great!", 5)};
        when(mockDAO.getReviewsByRating(5)).thenReturn(expected);

        Review[] actual = service.getReviewsByRating(5);

        assertArrayEquals(expected, actual);
        verify(mockDAO).getReviewsByRating(5);
    }

    @Test
    public void testCreateReview() throws IOException {
        Review toCreate = new Review(0, "Charlie", "Outstanding", 5);
        Review created = new Review(3, "Charlie", "Outstanding", 5);

        when(mockDAO.createReview(toCreate)).thenReturn(created);

        Review result = service.createReview(toCreate);
        assertEquals(created, result);
        verify(mockDAO).createReview(toCreate);
    }

    @Test
    public void testDeleteReview() throws IOException {
        when(mockDAO.deleteReview(1)).thenReturn(true);

        boolean result = service.deleteReview(1);
        assertTrue(result);
        verify(mockDAO).deleteReview(1);
    }

    @Test
    public void testDeleteAll() throws IOException {
        when(mockDAO.deleteAll()).thenReturn(true);
        boolean result = service.deleteAll();
        assertTrue(result);
        verify(mockDAO).deleteAll();
    }
}
