package com.ufund.api.ufundapi.service;

import java.io.IOException;

public interface DonationService {
    double getTotal() throws IOException;
    double getGoal() throws IOException;
    void addDonation(double amount) throws IOException;
    void setGoal(double goal) throws IOException;
}

