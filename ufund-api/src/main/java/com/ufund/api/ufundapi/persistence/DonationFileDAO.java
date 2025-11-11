package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Donation;

@Component
public class DonationFileDAO implements DonationDAO {
    private final ObjectMapper objectMapper;
    private final String filename;
    private Donation status;

    public DonationFileDAO(@Value("${donations.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Load in values from JSON into a MAP to do operations on
     * @throws IOException
     */
    private void load() throws IOException {
        status = objectMapper.readValue(new File(filename), Donation.class);
    }

    /**
     * Get the status/Data of the JSON
     */
    @Override
    public Donation getStatus() throws IOException {
        return status;
    }

    /**
     * Add the donation amount to the totalDOnated
     * @return The status/data
     */
    @Override
    public Donation addDonation(int amount) throws IOException {
        status.setTotalDonated(status.getTotalDonated() + amount);
        save(status);
        return status;
    }

    /**
     * Update goal 
     * retunr status/data
     */
    @Override
    public Donation setGoal(int newGoal) throws IOException {
        status.setDonationGoal(newGoal);
        save(status);
        return status;
    }
    /**
     * Save updated status/data to the JSON
     */
    @Override
    public void save(Donation status) throws IOException {
        this.status = status;
        objectMapper.writeValue(new File(filename), status);
    }
}
