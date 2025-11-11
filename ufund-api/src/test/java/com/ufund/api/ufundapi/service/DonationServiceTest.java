package com.ufund.api.ufundapi.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ufund.api.ufundapi.model.Donation;
import com.ufund.api.ufundapi.persistence.DonationDAO;
import com.ufund.api.ufundapi.persistence.DonationFileDAO;

/**
 * Unit tests for the DonationServiceImplementation class
 */
public class DonationServiceTest {
    private DonationServiceImplementation donationService;

    @Mock
    private DonationDAO mockDonationDAO;

    private Donation mockDonation;

    /**
     * Initialize the mock objects before each test
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        donationService = new DonationServiceImplementation(mockDonationDAO);

        mockDonation = new Donation();
        mockDonation.setDonationGoal(1000);
        mockDonation.setTotalDonated(200);
    }

    /**
     * Tests the getStatus method
     */
    @Test
    public void testGetStatus() throws IOException {
        when(mockDonationDAO.getStatus()).thenReturn(mockDonation);
        Donation result = donationService.getStatus();

        assertNotNull(result, "The returned Donation object should not be null");
        assertEquals(mockDonation, result, "The returned Donation should match the mockDonation object");
        verify(mockDonationDAO, times(1)).getStatus();
    }

    /**
     * Tests the addDonation method
     */
    @Test
    public void testAddDonation() throws IOException {
        when(mockDonationDAO.getStatus()).thenReturn(mockDonation);

        Donation updated = donationService.addDonation(300);

        assertEquals(500, updated.getTotalDonated(), "The total donated should be increased by 300");
        verify(mockDonationDAO, times(1)).getStatus();
        verify(mockDonationDAO, times(1)).save(updated);
    }

    /**
     * Tests the setGoal method with higher total donated amount
     */
    @Test
    public void testSetGoalHigherThanTotal() throws IOException {
        when(mockDonationDAO.getStatus()).thenReturn(mockDonation);
        Donation updated = donationService.setGoal(1500);

        assertEquals(1500, updated.getDonationGoal(), "The goal should be updated to the higher value");
        verify(mockDonationDAO, times(1)).getStatus();
        verify(mockDonationDAO, times(1)).setGoal(1500);
    }

    /**
     * Tests the setGoal method with a lower total donated amount
     */
    @Test
    public void testSetGoalLowerThanTotal() throws IOException {
        mockDonation.setTotalDonated(500);
        mockDonation.setDonationGoal(1000);

        when(mockDonationDAO.getStatus()).thenReturn(mockDonation);

        Donation updated = donationService.setGoal(300);

        assertEquals(500, updated.getDonationGoal(), "The goal should lowered to the new goal");
        verify(mockDonationDAO, times(1)).getStatus();
        verify(mockDonationDAO, times(1)).setGoal(500);
    }
}
