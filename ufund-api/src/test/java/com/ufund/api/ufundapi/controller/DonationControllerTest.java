package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOError;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ufund.api.ufundapi.model.Donation;
import com.ufund.api.ufundapi.service.DonationServiceImplementation;

/**
 * Unit tests for the DonationController
 */
public class DonationControllerTest {
    private DonationController donationController;

    @Mock
    private DonationServiceImplementation mockDonationService;
    private Donation mockDonation;

    /**
     * Initializes the mock objects before the tests
     */
    @BeforeEach 
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        donationController = new DonationController(mockDonationService);

        mockDonation = new Donation();
        mockDonation.setTotalDonated(500);
        mockDonation.setDonationGoal(1000);
    }

    /**
     * Tests the getStatus method
     */
    @Test
    public void testGetStatus() throws IOException {
        when(mockDonationService.getStatus()).thenReturn(mockDonation);
        Donation result = donationController.getStatus();

        assertNotNull(result);
        assertEquals(mockDonation, result);
        verify(mockDonationService, times(1)).getStatus();
    }

    /**
     * Tests the addDonation method
     */
    @Test
    public void testAddDonation() throws IOException {
        when(mockDonationService.addDonation(200)).thenReturn(mockDonation);
        Donation result = donationController.addDonation(200);

        assertNotNull(result);
        assertEquals(mockDonation, result);
        verify(mockDonationService, times(1)).addDonation(200);
    }

    /**
     * Tests the setGoal method
     */
    @Test
    public void testSetGoal() throws IOException {
        when(mockDonationService.setGoal(1500)).thenReturn(mockDonation);
        Donation result = donationController.setGoal(1500);

        assertNotNull(result);
        assertEquals(mockDonation, result);
        verify(mockDonationService, times(1)).setGoal(1500);
    }
}
