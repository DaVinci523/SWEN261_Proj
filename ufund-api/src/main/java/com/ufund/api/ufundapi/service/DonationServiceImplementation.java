package com.ufund.api.ufundapi.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ufund.api.ufundapi.model.Donation;
import com.ufund.api.ufundapi.persistence.DonationDAO;

@Service
public class DonationServiceImplementation {
    private final DonationDAO donationDAO;

    @Autowired
    public DonationServiceImplementation(DonationDAO donationDAO) {
        this.donationDAO = donationDAO;
    }

    /**
     * Get JSON status/data
     * @return
     * @throws IOException
     */
    public Donation getStatus() throws IOException {
        return donationDAO.getStatus();
    }

     /**
     * update donationTotal with the new donation
     * @param amount
     * @throws IOException
     */ 
    public Donation addDonation(int amount) throws IOException {
        Donation status = donationDAO.getStatus();

    // Add donation
    status.setTotalDonated(status.getTotalDonated() + amount);

    // Save changes
    donationDAO.save(status);

    return status;
    }

    /**
     * Update Goal
     * @param goal
     * @throws IOException
     */
    public Donation setGoal(int newGoal) throws IOException {
        Donation status = donationDAO.getStatus();

    // Prevent lowering the goal below what has already been raised
    if (newGoal < status.getTotalDonated()) {
        newGoal = status.getTotalDonated(); // clamp to minimum
    }

    status.setDonationGoal(newGoal);
    donationDAO.setGoal(newGoal); // save
    return status;
    }
    
}
