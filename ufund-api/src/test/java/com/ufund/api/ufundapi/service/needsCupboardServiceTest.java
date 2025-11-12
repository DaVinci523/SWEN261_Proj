package com.ufund.api.ufundapi.service;

import org.junit.jupiter.api.BeforeEach;

import com.ufund.api.ufundapi.persistence.NeedsCupboardDAO;

import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Need;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

/**
 * Unit test for the needsCupboardServiceImplementation
 */
public class needsCupboardServiceTest {
    private NeedsCupboardDAO mockDAO;

    private NeedsCupboardServiceImplementation service;

    /**
     * Initializes objects before each test
     */
    @BeforeEach
    public void setup() {
        mockDAO = mock(NeedsCupboardDAO.class);
        service = new NeedsCupboardServiceImplementation(mockDAO);
    }

    /**
     * Tests that NeedsCupboardServiceImplementation's getAllNeeds method retrieves all Need objects from the DAO
     */
    @Test
    public void testGetAllNeeds() throws IOException {
        Need[] expected = {
            new Need(1, "Wire", 5.0, 0, 10, "cord", true),
            new Need(2, "BreadBoard", 10.0, 0, 2, "board", true)
        };

        when(mockDAO.getNeeds()).thenReturn(expected);
        Need[] actual = service.getAllNeeds();
        
        assertArrayEquals(expected, actual);
        verify(mockDAO, times(1)).getNeeds();
    }

    /**
     * Tests that the getNeedByID method retrieves a single Need object from the DAO based on the ID
     */
    @Test
    public void testGetNeedById() throws IOException {
        Need expected = new Need(1, "Sensor", 3.5, 0, 4, "component", true);

        when(mockDAO.getNeed(1)).thenReturn(expected);
        Need actual = service.getNeedById(1);
        
        assertEquals(expected, actual);
        verify(mockDAO, times(1)).getNeed(1);
    }

    /**
     * Tests that the createNeed method correctly creates a new Need through the DAO
     */
    @Test
    public void testCreateNeed() throws IOException {
        Need toCreate = new Need(0, "Battery", 12.0, 0, 1, "power", true);
        Need created = new Need(5, "Battery", 12.0, 0, 1, "power", true);

        when(mockDAO.createdNeed(toCreate)).thenReturn(created);

        Need result = service.createNeed(toCreate);
        
        assertEquals(created, result);
        verify(mockDAO).createdNeed(toCreate);
    }

    /**
     * Tests the deleteNeedById method correctly removes a Need by its ID
     */
    @Test
    public void testDeleteNeedById() throws IOException {
        when(mockDAO.deleteNeed(3)).thenReturn(true);
        boolean result = service.deleteNeedById(3);
        
        assertTrue(result);
        verify(mockDAO).deleteNeed(3);
    }

    /**
     * Tests that the deleteNeedById method correctly handles an IOException thrown by the DAO.
     */
    @Test
    public void testDeleteNeedThrowsIOException() throws IOException {
        when(mockDAO.deleteNeed(99)).thenThrow(new IOException("Disk error"));
        
        assertThrows(IOException.class, () -> service.deleteNeedById(99));
        verify(mockDAO).deleteNeed(99);
    }

    /**
     * Tests that searchNeeds method retrieves Need objects matching a given name
     */
    @Test
    public void testSearchNeeds() throws IOException {
        Need[] expected = {
            new Need(1, "Wire", 5.0, 0, 10, "cord", true)
        };

        when(mockDAO.searchNeeds("Wire")).thenReturn(expected);

        Need[] actual = service.searchNeeds("Wire");

        assertArrayEquals(expected, actual);
        verify(mockDAO).searchNeeds("Wire");
    }

    /**
     * Tests that updateNeed method updates a Need through the DAO
     */
    @Test
    public void testUpdateNeed() throws IOException {
        Need toUpdate = new Need(2, "BreadBoard", 12.0, 0, 3, "board", true);
        Need updated = new Need(2, "BreadBoard", 12.0, 0, 3, "board", true);

        when(mockDAO.updateNeed(toUpdate)).thenReturn(updated);

        Need result = service.updateNeed(toUpdate);

        assertEquals(updated, result);
        verify(mockDAO).updateNeed(toUpdate);
    }

    /**
     * Tests that deleteNeedByType method removes all Needs of a given type
     */
    @Test
    public void testDeleteNeedByType() throws IOException {
        when(mockDAO.deleteNeedType("board")).thenReturn(true);

        boolean result = service.deleteNeedByType("board");

        assertTrue(result);
        verify(mockDAO).deleteNeedType("board");
    }

    /**
     * Tests that deleteNeedByName method removes a Need with a given name
     */
    @Test
    public void testDeleteNeedByName() throws IOException {
        when(mockDAO.deleteNeedName("Wire")).thenReturn(true);

        boolean result = service.deleteNeedByName("Wire");

        assertTrue(result);
        verify(mockDAO).deleteNeedName("Wire");
    }

    /**
     * Tests that deleteAllNeeds method removes all Needs in the DAO
     */
    @Test
    public void testDeleteAllNeeds() throws IOException {
        when(mockDAO.deleteAll()).thenReturn(true);

        boolean result = service.deleteAllNeeds();

        assertTrue(result);
        verify(mockDAO).deleteAll();
    }

    /**
     * Tests that searchNeedsType method gets Needs by the type with the DAO
     */
    @Test
    public void testSearchNeedsType() throws IOException {
        Need[] expected = {
            new Need(1, "BreadBoard", 10.0, 0, 3, "board", true)
        };

        when(mockDAO.searchNeedsType("board")).thenReturn(expected);

        Need[] actual = service.searchNeedsType("board");

        assertArrayEquals(expected, actual);
        verify(mockDAO, times(1)).searchNeedsType("board");
    }

    /**
     * Tests searchNeedsQuantity method
     */
    @Test
    public void testSearchNeedsQuantity() throws IOException {
        Need[] expected = {
            new Need(2, "Wire", 5.0, 0, 10, "cord", true),
            new Need(3, "Resistor", 2.0, 0, 100, "component", true)
        };

        when(mockDAO.searchNeedsQunatity(5)).thenReturn(expected);

        Need[] actual = service.searchNeedsQuantity(5);

        assertArrayEquals(expected, actual);
        verify(mockDAO, times(1)).searchNeedsQunatity(5);
    }

    /**
     * Tests searchNeedsValue method
     */
    @Test
    public void testSearchNeedsValue() throws IOException {
        Need[] expected = {
            new Need(4, "Sensor", 3.5, 0, 4, "component", true)
        };

        when(mockDAO.searchNeedsValue(3.5)).thenReturn(expected);

        Need[] actual = service.searchNeedsValue(3.5);

        assertArrayEquals(expected, actual);
        verify(mockDAO, times(1)).searchNeedsValue(3.5);
    }
    
}
