package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Review;
import com.ufund.api.ufundapi.service.ReviewService;

/**
 * Handles the REST API requests for reviews
 * 
 * @author Anthony Pelto
 */
@RestController
@RequestMapping("reviews")
public class ReviewController {
    private static final Logger LOG = Logger.getLogger(ReviewController.class.getName());
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Get reviews
     */
    @GetMapping("")
    public ResponseEntity<Review[]> getReviews() {
        LOG.info("Get /reviews");
        try {
            Review[] reviews = reviewService.getReviews();
            if (reviews.length > 0){
                return new ResponseEntity<>(reviews, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a review by its id
     */
    @GetMapping("/rating/{rating}")
    public ResponseEntity<Review[]> getReviewsByRating(@PathVariable int rating) {
        LOG.info("Get /reviews/rating/" + rating);
        try {
            Review[] reviews = reviewService.getReviewsByRating(rating);
            if (reviews.length > 0){
                return new ResponseEntity<>(reviews, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST for creating reviews
     */
    @PostMapping("")
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        LOG.info("POST /reviews " + review);
        try {
            Review newReview = reviewService.createReview(review);
            if (newReview != null){
                return new ResponseEntity<>(newReview, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } 
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a review by its id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Review> deleteReview(@PathVariable int id) {
        LOG.info("DELETE /reviews/ " + id);
        try {
            if (reviewService.deleteReview(id)){
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } 
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     /**
     * Delete all the reviews
     */
    @DeleteMapping("")
    public ResponseEntity<Review> deleteAllReviews() {
        LOG.info("DELETE /reviews");
        try {
            if (reviewService.deleteAll()){
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } 
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
