package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Donation;

public interface DonationDAO {
    /**
     * Gather the status of donation JSON
     * @return
     * @throws IOException
     */
    Donation getStatus() throws IOException;

    /**
     * Add donation to the totalDonations
     * @param amount
     * @return Donation
     * @throws IOException
     */
    Donation addDonation(int amount) throws IOException;

    /**
     * Update the donationGoal
     * @param newGoal
     * @return
     * @throws IOException
     */
    Donation setGoal(int newGoal) throws IOException;

    /**
     * Save to JSON
     * @param status
     * @throws IOException
     */
    void save(Donation status) throws IOException;
}

