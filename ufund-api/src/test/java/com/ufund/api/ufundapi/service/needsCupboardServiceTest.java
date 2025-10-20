package com.ufund.api.ufundapi.service;

import org.junit.jupiter.api.BeforeEach;

import com.ufund.api.ufundapi.persistence.NeedsCupboardDAO;
import com.ufund.api.ufundapi.service.NeedsCupboardServiceImplementation;

import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Need;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOError;
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
     * Tests that NeedsCupboardServiceImplementation's getAllNeeds() method retrieves
     * all Need objects from the DAO
     */
    @Test
    public void testGetAllNeeds() throws IOException {
        Need[] expected = {
            new Need(1, "Wire", 5.0, 10, "cord"),
            new Need(2, "BreadBoard", 10.0, 2, "board")
        };

        when(mockDAO.getNeeds()).thenReturn(expected);
        Need[] actual = service.getAllNeeds();
        
        assertArrayEquals(expected, actual);
        verify(mockDAO, times(1)).getNeeds();
    }

    /**
     * Tests that NeedsCupboardServiceImplementation's getNeedByID() method retrieves
     * a single Need object from the DAO based on the ID
     */
    @Test
    public void testGetNeedById() throws IOException {
        Need expected = new Need(1, "Sensor", 3.5, 4, "component");

        when(mockDAO.getNeed(1)).thenReturn(expected);
        Need actual = service.getNeedById(1);
        
        assertEquals(expected, actual);
        verify(mockDAO, times(1)).getNeed(1);
    }

    /**
     * Tests that NeedsCupboardServiceImplementation's createNeed() correctly creates
     * a new Need through the DAO
     */
    @Test
    public void testCreateNeed() throws IOException {
        Need toCreate = new Need(0, "Battery", 12.0, 1, "power");
        Need created = new Need(5, "Battery", 12.0, 1, "power");

        when(mockDAO.createdNeed(toCreate)).thenReturn(created);

        Need result = service.createNeed(toCreate);
        
        assertEquals(created, result);
        verify(mockDAO).createdNeed(toCreate);
    }

    /**
     * Tests that NeedsCupboardServiceImplementation's deleteNeedById correctly
     *removes a Need by its ID
     */
    @Test
    public void testDeleteNeedById() throws IOException {
        when(mockDAO.deleteNeed(3)).thenReturn(true);
        boolean result = service.deleteNeedById(3);
        
        assertTrue(result);
        verify(mockDAO).deleteNeed(3);
    }

    /**
     * Tests that NeedsCupboardServiceImplementation's deleteNeedById correctly
     * propogates an IOException thrown by the DAO.
     */
    @Test
    public void testDeleteNeedThrowsIOException() throws IOException {
        when(mockDAO.deleteNeed(99)).thenThrow(new IOException("Disk error"));
        
        assertThrows(IOException.class, () -> service.deleteNeedById(99));
        verify(mockDAO).deleteNeed(99);
    }
}
