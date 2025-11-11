package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Donation model class
 */
public class DonationTest {
    private Donation donation;

    /**
     * Initializes a new Donation object before each test
     */
    @BeforeEach
    public void setUp() {
        donation = new Donation(500, 1000);
    }

    /**
     * Tests the Donation constructor
     */
    @Test
    public void testDonationConstructor() {
        assertEquals(500, donation.getTotalDonated());
        assertEquals(1000, donation.getDonationGoal());
    }

    /**
     * Tests the getTotalDonated method
     */
    @Test
    public void testGetTotalDonated() {
        donation.setTotalDonated(750);
        assertEquals(750, donation.getTotalDonated());
    }

    /**
     * Tests the getDonationGoal method
     */
    @Test
    public void testGetDonationGoal() {
        donation.setDonationGoal(2000);
        assertEquals(2000, donation.getDonationGoal());
    }
}
