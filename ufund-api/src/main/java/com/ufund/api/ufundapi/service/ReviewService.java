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
     * Gets all reviews with a specific rating
     * @param rating the rating to filer by
     * @return  an arrray of reviews with the same rating
     * @throws IOException if an I/O error happens
     */
    Review[] getReviewsByRating(int rating) throws IOException;

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
     * Delete any reviews by the username
     * @param userName the username whose reviews are being deleted
     * @return true if the file was updated successfully or false if it was not
     * @throws IOException if an I/O error happens
     */
    boolean deleteReviewsByUsername(String userName) throws IOException;

    /**
     * Delete all reviews
     * @return true if all review were deleted
     * @throws IOException if an I/O error happens
     */
    boolean deleteAll() throws IOException;
}
