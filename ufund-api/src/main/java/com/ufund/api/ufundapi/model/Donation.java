package com.ufund.api.ufundapi.model;

public class Donation {
    private int totalDonated;
    private int donationGoal;

    public Donation() {}

    public Donation(int totalDonated, int donationGoal) {
        this.totalDonated = totalDonated;
        this.donationGoal = donationGoal;
    }

    public int getTotalDonated() { return totalDonated; }
    public void setTotalDonated(int totalDonated) { this.totalDonated = totalDonated; }

    public int getDonationGoal() { return donationGoal; }
    public void setDonationGoal(int donationGoal) { this.donationGoal = donationGoal; }
}
