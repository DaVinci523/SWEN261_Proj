package com.ufund.api.ufundapi.model;

public class Donation {
    private int totalDonated;
    private int donationGoal;

    public Donation() {}
    /**
     * Object type Donation
     * @param totalDonated
     * @param donationGoal
     */
    public Donation(int totalDonated, int donationGoal) {
        this.totalDonated = totalDonated;
        this.donationGoal = donationGoal;
    }
    /**
     * Return TotalDonated Value
     * @return totalDonation
     */
    public int getTotalDonated() { return totalDonated; }
    /**
     * Setter for totalDonated
     * @param totalDonated
     */
    public void setTotalDonated(int totalDonated) { this.totalDonated = totalDonated; }

    /**
     * Get the donationGoal
     * @return donationGoal
     */
    public int getDonationGoal() { return donationGoal; }
    /**
     * Set Donation goal
     * @param donationGoal
     */
    public void setDonationGoal(int donationGoal) { this.donationGoal = donationGoal; }
}
