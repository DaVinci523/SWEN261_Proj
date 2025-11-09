package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Donation;

public interface DonationDAO {
    Donation getStatus() throws IOException;
    Donation addDonation(int amount) throws IOException;
    Donation setGoal(int newGoal) throws IOException;
    void save(Donation status) throws IOException;
}

