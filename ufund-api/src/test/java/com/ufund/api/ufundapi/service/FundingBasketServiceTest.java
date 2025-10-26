package com.ufund.api.ufundapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.FundingBasketDAO;
import com.ufund.api.ufundapi.service.FundingBasketServiceImplementation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

/**
 * Unit tests for the FundingBasketServiceImplementation
 * 
 * @author Anthony Pelto
 */
public class FundingBasketServiceTest {
    private FundingBasketDAO mockDAO;
    private FundingBasketServiceImplementation service;

    private static final String USERNAME = "user1";

    /**
     * Initialize the mock DAO and service before each test
     */
    @BeforeEach
    public void setup() {
        mockDAO = mock(FundingBasketDAO.class);
        service = new FundingBasketServiceImplementation(mockDAO);
    }

    /**
     * Tests that getNeeds() returns all the needs correctly
     */
    @Test
    public void testGetNeeds() throws IOException {
        Need[] expected = {
            new Need(1, "Wires", 5.0, 0, 10, "cord", true),
            new Need(2, "Breadboard", 12.0, 0, 3, "board", true)
        };

        when(mockDAO.getNeeds(USERNAME)).thenReturn(expected);
        Need[] actual = service.getNeeds(USERNAME);

        assertArrayEquals(expected, actual);
        verify(mockDAO, times(1)).getNeeds(USERNAME);
    }

    /**
     * Tests that the FundingBasketService getNeed returns
     * a single need by its ID.
     */
    @Test
    public void testGetNeed() throws IOException {
        Need expected = new Need(1, "Sensor", 3.5, 0, 4, "component", true);
        when(mockDAO.getNeed(USERNAME, 1)).thenReturn(expected);
        Need actual = service.getNeed(USERNAME, 1);

        assertEquals(expected, actual);
        verify(mockDAO, times(1)).getNeed(USERNAME, 1);
    }

    /**
     * Tests searchNeeds to verify the needs are filtered by name
     */
    @Test
    public void testSearchNeeds() throws IOException {
        String searchTerm = "Lab";
        Need[] expected = {
            new Need(1, "Lab Kit", 20.0, 0, 2, "kit", true),
            new Need(2, "Lab Coat", 15.0, 0, 5, "apparel", true)
        };

        when(mockDAO.searchNeeds(USERNAME, searchTerm)).thenReturn(expected);
        Need[] actual = service.searchNeeds(USERNAME, searchTerm);

        assertArrayEquals(expected,actual);
        verify(mockDAO, times(1)).searchNeeds(USERNAME, searchTerm);
    }

    /**
     * Tests createdNeed to verify that a new need is successfully created
     */
    @Test
    public void testCreateNeed() throws IOException {
        Need newNeed = new Need(3, "Resistor", 0.10, 0, 100, "component", true);
        when(mockDAO.createdNeed(USERNAME, newNeed)).thenReturn(newNeed);

        Need result = service.createdNeed(USERNAME, newNeed);
        assertEquals(newNeed, result);
        verify(mockDAO, times(1)).createdNeed(USERNAME, newNeed);
    }

    /**
     * Tests that updateNeed() updates an existing need
     */
    @Test
    public void testUpdateNeed() throws IOException {
        Need updated = new Need(2, "Updated Board", 14.0, 0, 2, "board", true);
        when(mockDAO.updateNeed(USERNAME, updated)).thenReturn(updated);
        Need result = service.updateNeed(USERNAME, updated);

        assertEquals(updated, result);
        verify(mockDAO, times(1)).updateNeed(USERNAME, updated);
    }

    /**
     * Tests that deleteNeed(int) deletes a need by its ID
     */
    @Test
    public void testDeleteNeed() throws IOException {
        when(mockDAO.deleteNeed(USERNAME,1)).thenReturn(true);
        boolean result = service.deleteNeed(USERNAME, 1);

        assertTrue(result);
        verify(mockDAO, times(1)).deleteNeed(USERNAME, 1);
    }

    /**
     * Tests that deleteNeedType deletes all needs of a given type
     */
    @Test
    public void testDeleteNeedType() throws IOException {
        when(mockDAO.deleteNeedType(USERNAME, "board")).thenReturn(true);
        boolean result = service.deleteNeedType(USERNAME, "board");

        assertTrue(result);
        verify(mockDAO, times(1)).deleteNeedType(USERNAME, "board");
    }

    /**
     * Tests that deleteAll() deletes all Needs
     */
    @Test
    public void testDeleteAll() throws IOException {
        when(mockDAO.deleteAll(USERNAME)).thenReturn(true);
        boolean result = service.deleteAll(USERNAME);

        assertTrue(result);
        verify(mockDAO, times(1)).deleteAll(USERNAME);
    }

    /**
     * Tests that deleteNeedname deletes needs with a matching substring
     */
    @Test
    public void testDeleteNeedName() throws IOException {
        when(mockDAO.deleteNeedName(USERNAME, "Wire")).thenReturn(true);
        boolean result = service.deleteNeedName(USERNAME, "Wire");

        assertTrue(result);
        verify(mockDAO, times(1)).deleteNeedName(USERNAME, "Wire");
    }

    /**
     * Tests that getUsers() returns all users correctly
     */
    @Test
    public void testGetUsers() throws IOException {
        User[] expected = {
            new User("user1", new Need[] {
                new Need(1, "Wire", 5.0, 0, 10, "cord", true)
            }),
            new User("user2", new Need[] {
                new Need(2, "Breadboard", 12.0, 0, 3, "board", true)
            })
        };

        when(mockDAO.getUsers()).thenReturn(expected);

        User[] actual = service.getUsers();

        assertArrayEquals(expected, actual);
        verify(mockDAO, times(1)).getUsers();
    }
}
