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

    private void load() throws IOException {
        status = objectMapper.readValue(new File(filename), Donation.class);
    }

    @Override
    public Donation getStatus() throws IOException {
        return status;
    }

    @Override
    public Donation addDonation(int amount) throws IOException {
        status.setTotalDonated(status.getTotalDonated() + amount);
        save(status);
        return status;
    }

    @Override
    public Donation setGoal(int newGoal) throws IOException {
        status.setDonationGoal(newGoal);
        save(status);
        return status;
    }
    @Override
    public void save(Donation status) throws IOException {
        this.status = status;
        objectMapper.writeValue(new File(filename), status);
    }
}
