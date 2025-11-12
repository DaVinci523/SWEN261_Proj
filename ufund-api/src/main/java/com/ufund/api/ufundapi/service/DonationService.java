package com.ufund.api.ufundapi.service;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Donation;

public interface DonationService {

    /**
     * Get JSON status/data
     * @return
     * @throws IOException
     */
    Donation getStatus() throws IOException;
    /**
     * update donationTotal with the new donation
     * @param amount
     * @throws IOException
     */
    Donation addDonation(int amount) throws IOException;

    /**
     * Update Goal
     * @param goal
     * @throws IOException
     */
    Donation setGoal(int newGoal) throws IOException;
}

