package com.ufund.api.ufundapi.service;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Review;

/**
 * Service interface for review managing
 * @author Anthony Pelto
 */
public interface ReviewService {
    /**
     * Get all the reviews
     * @return array of all Review objects
     * @throws IOException if an I/O error happens
     */
    Review[] getReviews() throws IOException;

    /**
     * Gets a single review by its id
     * @param id the unique id of the review
     * @return the Review if it exists or null if it doesn't
     * @throws IOException if an I/O error happens
     */
    Review getReview(int id) throws IOException;

    /**
     * Create a new review
     * 
     * @param review the Review object to be created
     * @return the new review created
     * @throws IOException if an I/O error happens
     */
    Review createReview(Review review) throws IOException;

    /**
     * Delete a review by its ID
     * @param id the ID of the review to delete
     * @throws IOException if an I/O error happens
     */
    boolean deleteReview(int id) throws IOException;

    /**
     * Delete all reviews
     * @return true if all review were deleted
     * @throws IOException if an I/O error happens
     */
    boolean deleteAll() throws IOException;
}
