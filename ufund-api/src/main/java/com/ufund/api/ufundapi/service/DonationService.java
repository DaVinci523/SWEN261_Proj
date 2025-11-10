package com.ufund.api.ufundapi.service;

import java.io.IOException;

public interface DonationService {

    /**
     * Get totalDonated
     * @return donationTotal
     * @throws IOException
     */
    double getTotal() throws IOException;

    /**
     * Get donationGoal
     * @return goal
     * @throws IOException
     */
    double getGoal() throws IOException;

    /**
     * update donationTotal with the new donation
     * @param amount
     * @throws IOException
     */
    void addDonation(double amount) throws IOException;

    /**
     * Update Goal
     * @param goal
     * @throws IOException
     */
    void setGoal(double goal) throws IOException;
}

