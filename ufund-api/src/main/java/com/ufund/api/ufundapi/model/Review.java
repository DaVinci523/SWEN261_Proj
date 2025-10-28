package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Review left by a user. Each review contains:
 * -an id
 * -the username of the reviewer
 * -the text body of the review
 * -an int rating
 * @author Anthony Pelto
 */
public class Review {
    private static final Logger LOG = Logger.getLogger(Review.class.getName());
    static final String STRINGIFY = "Review [id=%d, userName=%s, rating=%d, reviewText=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("userName") private String userName;
    @JsonProperty("reviewText") private String reviewText;
    @JsonProperty("rating") private int rating;

    /**
     * Create a Review with the given parameters
     * 
     * @param id the identifier for the Review
     * @param userName the name of the user who wrote the review
     * @param reviewText the text content of the review
     * @param rating the integer rating
     */
    public Review(@JsonProperty("id") int id, @JsonProperty("userName") String userName, @JsonProperty("reviewText") String reviewText, @JsonProperty("rating") int rating){
        this.id = id;
        this.userName = userName;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    // Getters //
    public int getId(){
        return id;
    }

    public String getUserName(){
        return userName;
    }

    public String getReviewText(){
        return reviewText;
    }

    public int getRating(){
        return rating;
    }

    // Setters to allow review editing //
    public void setReviewText(String reviewText){
        this.reviewText = reviewText;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    /**
     * @return the String representation of a review
     */
    @Override
    public String toString(){
        return String.format(STRINGIFY, id, userName, rating, reviewText);
    }
}
