package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Review;

/**
 * Implements JSON file persistence for the review objects
 * @author Anthony Pelto
 */
@Component
public class ReviewFileDAO implements ReviewDAO{
    private static final Logger LOG = Logger.getLogger(ReviewDAO.class.getName());
    private Map<Integer, Review> reviews;
    private ObjectMapper objectMapper;
    private static int nextId;
    private String filename;

    /**
     * Creates a Review DAO
     * @param filename the Filename to work from
     * @param objectMapper provides the JSON serialization/deserialization
     * @throws IOException when the file cannot be accessed
     */
    public ReviewFileDAO(@Value("${reviews.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Generate the next unique review ID
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Return an array of all reviews
     */
    private Review[] getReviewsArray() {
        return reviews.values().toArray(new Review[0]);
    }

    /**
     * Save all reviews to the JSON file
     */
    private boolean save() throws IOException {
        Review[] reviewArray = getReviewsArray();
        objectMapper.writeValue(new File(filename), reviewArray);
        return true;
    }

    /**
     * Load reviews from the JSON file into a TreeMap
     */
    private boolean load() throws IOException {
        reviews = new TreeMap<>();
        nextId = 0;

        File file = new File(filename);
        if (file.exists() && file.length() > 0){
            Review[] reviewArray = objectMapper.readValue(file, Review[].class);
            for (Review review : reviewArray) {
                reviews.put(review.getId(), review);
                if (review.getId() > nextId){
                    nextId = review.getId();
                }
            }
            ++nextId;
        }
        return true;
    }

    /**
     * Returns an array of all reviews in the JSON
     * @return Review[]
     */
    @Override
    public Review[] getReviews() {
        synchronized (reviews) {
            return getReviewsArray();
        }
    }

    /**
     * Returns a single review by its id
     * @param id the id to search for
     * @return the review if it exists or null if it does not
     */
    @Override
    public Review[] getReviewsByRating(int rating) throws IOException {
        synchronized (reviews) {
            return reviews.values().stream().filter(r -> r.getRating() == rating).toArray(Review[]::new);
        }
    }

    /**
     * Create a new Review object
     * @param review the input review object
     * @return the new Review with an id
     * @throws IOException if an error occurs saving the new Review object
     */
    @Override
    public Review createReview(Review review) throws IOException {
        synchronized (reviews) {
            Review newReview = new Review(nextId(), review.getUserName(), review.getReviewText(), review.getRating());
            reviews.put(newReview.getId(), newReview);
            save();
            return newReview;
        }
    }

    /**
     * Delete a review at the specified id
     * @param id the reviews id
     * @return true if the file was updated successfully or false if it was not
     */
    @Override
    public boolean deleteReview(int id) throws IOException {
        synchronized (reviews) {
            if (reviews.containsKey(id)) {
                reviews.remove(id);
                return save();
            } else {
                return false;
            }
        }
    }

    /**
     * Deletes any reviews from the given username
     * @param userName the username whose reviews should be deleted
     * @return true if the file was updated sucessfully or false if it was not
     * @throws IOException
     */
    @Override
    public boolean deleteReviewsByUsername(String userName) throws IOException {
        synchronized (reviews) {
            boolean removedAny = reviews.values().removeIf(r -> r.getUserName().equals(userName));
            if (removedAny) {
                return save();
            }
            return false;
        }
    }

    /**
     * Delete all reviews
     * @return true if the file was updated successfully or false if it was not
     */
    @Override
    public boolean deleteAll() throws IOException {
        synchronized (reviews) {
            reviews.clear();
            return save();
        }
    }
    
}
