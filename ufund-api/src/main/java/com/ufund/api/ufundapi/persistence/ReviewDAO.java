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
     * Returns all reviews of a certain rating
     * @param rating the rating value to filter reviews by
     * @return an array of reviews that have the same rating as the parameter
     */
    Review[] getReviewsByRating(int rating) throws IOException;

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
     * Deletes any reviews from the given username
     * @param userName the username whose reviews should be deleted
     * @return true if the file was updated sucessfully or false if it was not
     * @throws IOException
     */
    boolean deleteReviewsByUsername(String userName) throws IOException;

    /**
     * Delete all reviews
     * @return true if the file was updated successfully or false if it was not
     */
    boolean deleteAll() throws IOException;


}
