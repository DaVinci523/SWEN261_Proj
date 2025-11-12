package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Donation;

/**
 * Unit tests for the DonationFileDAO class
 */
public class DonationFileDAOTest {
    private DonationFileDAO donationFileDAO;

    @Mock
    private ObjectMapper mockObjectMapper;

    private Donation mockDonation;
    private final String testFilename = "test_donations.json";

    /**
     * Initialized the DAO before each test
     */
    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        mockDonation = new Donation(200, 1000);

        //Mock the load call
        when(mockObjectMapper.readValue(any(File.class),eq(Donation.class))).thenReturn(mockDonation);

        donationFileDAO = new DonationFileDAO(testFilename, mockObjectMapper);
    }

    /**
     * Tests the getStatus method
     */
    @Test
    public void testGetStatus() throws IOException {
        Donation result = donationFileDAO.getStatus();
        assertNotNull(result);
        assertEquals(mockDonation, result);
    }

    /**
     * Tests the addDonation method
     */
    @Test
    public void testAddDonation() throws IOException {
        Donation result = donationFileDAO.addDonation(300);
        assertEquals(500, result.getTotalDonated());
        verify(mockObjectMapper, times(1)).writeValue(any(File.class), eq(mockDonation));
    }

    /**
     * Tests the setGoal method
     */
    @Test
    public void testSetGoal() throws IOException {
        Donation result = donationFileDAO.setGoal(2000);
        assertEquals(2000, result.getDonationGoal());
        verify(mockObjectMapper, times(1)).writeValue(any(File.class), eq(mockDonation));
    }

}
