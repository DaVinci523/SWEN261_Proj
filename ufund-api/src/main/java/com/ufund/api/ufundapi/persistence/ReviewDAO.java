package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Review;

/**
 * Interface for Review objects 
 * @author Anthony Pelto
 */
public interface ReviewDAO {
    /**
     * Returns an array of all reviews in the JSON
     * @return Review[]
     */
    Review[] getReviews() throws IOException;

    /**
     * Returns a single review by its id
     * @param id the id to search for
     * @return the review if it exists or null if it does not
     */
    Review getReview(int id) throws IOException;

    /**
     * Create a new Review object
     * @param review the input review object
     * @return the new Review with an id
     * @throws IOException if an error occurs saving the new Review object
     */
    Review createReview(Review review) throws IOException;

    /**
     * Delete a review at the specified id
     * @param id the reviews id
     * @return true if the file was updated successfully or false if it was not
     */
    boolean deleteReview(int id) throws IOException;

    /**
     * Delete all reviews
     * @return true if the file was updated successfully or false if it was not
     */
    boolean deleteAll() throws IOException;

}
