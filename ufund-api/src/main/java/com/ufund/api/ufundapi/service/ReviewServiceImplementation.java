package com.ufund.api.ufundapi.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.model.Review;
import com.ufund.api.ufundapi.persistence.ReviewDAO;

/**
 * Implementation of the ReviewService interface
 * @author Anthony Pelto
 */
@Service
public class ReviewServiceImplementation implements ReviewService {
    private final ReviewDAO reviewDAO;

    @Autowired
    public ReviewServiceImplementation(ReviewDAO reviewDAO){
        this.reviewDAO = reviewDAO;
    }

    @Override
    public Review[] getReviews() throws IOException {
        return reviewDAO.getReviews();
    }

    @Override
    public Review[] getReviewsByRating(int rating) throws IOException {
        return reviewDAO.getReviewsByRating(rating);
    }

    @Override
    public Review createReview(Review review) throws IOException {
        return reviewDAO.createReview(review);
    }

    @Override
    public boolean deleteReview(int id) throws IOException {
        return reviewDAO.deleteReview(id);
    }

    @Override
    public boolean deleteAll() throws IOException {
        return reviewDAO.deleteAll();
    }
}
