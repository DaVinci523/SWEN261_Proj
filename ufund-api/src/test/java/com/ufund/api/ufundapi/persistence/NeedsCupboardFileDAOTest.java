package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

/**
 * Unit tests for the NeedsCupboardFileDAO class
 */
public class NeedsCupboardFileDAOTest {
    private NeedsCupboardFileDAO dao;
    private ObjectMapper mockObjectMapper;
    private Need[] testNeeds;

    /**
     * Setup the mock ObjectMapper and DAO before each test
     */
    @BeforeEach
    public void setup() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testNeeds = new Need[] {
            new Need(1, "Wire", 5.0, 0, 10, "cord", true),
            new Need(2, "BreadBoard", 10.0, 0, 2, "board", true),
            new Need(3, "Sensor", 3.5, 0, 4, "component", true)
        };

        // Mock the ObjectMapper to return our testNeeds array
        when(mockObjectMapper.readValue(any(File.class), eq(Need[].class)))
            .thenReturn(testNeeds);

        dao = new NeedsCupboardFileDAO("doesnt_matter.json", mockObjectMapper);
    }

    /**
     * Tests getNeeds() method
     */
    @Test
    public void testGetNeeds() throws IOException {
        Need[] needs = dao.getNeeds();
        assertEquals(testNeeds.length, needs.length);
        for (int i = 0; i < testNeeds.length; i++){
            assertEquals(testNeeds[i], needs[i]);
        }
    }

    /**
     * Test that getNeed() method returns a single Need by its ID
     */
    @Test
    public void testGetNeed() throws IOException {
        Need need = dao.getNeed(1);
        assertEquals(testNeeds[0], need);
    }

    /**
     * Test that getNeed() method returns null if the ID does not exist
     */
    @Test
    public void testGetNeedNotFound() throws IOException{
        Need need = dao.getNeed(999);
        assertNull(need);
    }

    /**
     * Test that searchNeeds method only returns Needs that contain the substring
     */
    @Test
    public void testSearchNeeds() throws IOException {
        Need[] needs = dao.searchNeeds("Bread");
        assertEquals(1, needs.length);
        assertEquals(testNeeds[1], needs[0]);
    }

    /**
     * Tests the createNeed method
     */
    @Test
    public void testCreateNeed() throws IOException {
        Need newNeed = new Need(0, "Battery", 12.0, 0, 1, "power", true);
        Need result = dao.createdNeed(newNeed);
        assertNotNull(result);
        assertEquals(newNeed.getName(), result.getName());
        assertEquals(newNeed.getValue(), result.getValue());
        assertEquals(newNeed.getType(), result.getType());
    }
    
    @Test
    public void testCreateNeedNotExist() throws IOException {
        Need newNeed = new Need(0, "Wire", 12.0, 0, 1, "power", true);
        Need result = dao.createdNeed(newNeed);
        assertNull(result);
    }

    /**
     * Tests that updateNeed method updates an existing need
     */
    @Test
    public void testUpdateNeed() throws IOException {
        Need updated = new Need(2, "Updated Board", 15.0, 0, 5, "board", true);
        Need result = dao.updateNeed(updated);
        assertEquals(updated, result);
        assertEquals(updated, dao.getNeed(2));
    }

    /**
     * Test that updateNeed method returns null for non existent needs
     */
    @Test
    public void testUpdateNeedNotFound() throws IOException {
        Need updated = new Need(999, "NonExistent", 0.0, 0, 0, "none", true);
        Need result = dao.updateNeed(updated);
        assertNull(result);
    }

    /**
     * Tests that deleteNeed method removes the need and returns true
     */
    @Test
    public void testDeleteNeed() throws IOException {
        boolean result = dao.deleteNeed(1);
        assertTrue(result);
        assertNull(dao.getNeed(1));
    }

    /**
     * Tests that deleteNeed method returns false if the need does not exist
     */
    @Test
    public void testDeleteNeedNotFound() throws IOException {
        boolean result = dao.deleteNeed(999);
        assertFalse(result);
    }

    /**
     * Test deleteNeedType method 
     */
    @Test
    public void testDeleteNeedType() throws IOException {
        boolean result = dao.deleteNeedType("board");
        assertTrue(result);
        for (Need n : dao.getNeeds()) {
            assertNotEquals("board", n.getType());
        }
    }

    @Test
    public void testDeleteNeedTypeFalse() throws IOException {
        boolean result = dao.deleteNeedType("rabbit");
        assertFalse(result);
    }

    /**
     * Test deleteAll method
     */
    @Test
    public void testDeleteAll() throws IOException {
        boolean result = dao.deleteAll();
        assertTrue(result);
        assertEquals(0, dao.getNeeds().length);
    }

    /**
     * Test deletNeedName method
     */
    @Test
    public void testDeleteNeedName() throws IOException {
        boolean result = dao.deleteNeedName("Wire");
        assertTrue(result);
        for (Need n : dao.getNeeds()){
            assertFalse(n.getName().contains("Wire"));
        }
    }

    @Test
    public void testDeleteNeedNameFalse() throws IOException {
        boolean result = dao.deleteNeedName("Cobra");
        assertFalse(result);
    }

    /**
     * Test searchNeedsType method
     */
    @Test
    public void testSearchNeedsType() throws IOException {
        Need[] results = dao.searchNeedsType("board");
        assertEquals(1, results.length);
        assertEquals("BreadBoard", results[0].getName());
    }

    @Test
    public void testSearchNeedsTypeNull() throws IOException {
        Need[] results = dao.searchNeedsType(null);
        assertEquals(testNeeds.length, results.length);
    }

    @Test
    public void testSearchNeedsTypeNoMatch() throws IOException {
        Need[] results = dao.searchNeedsType("nonexistent");
        assertEquals(0, results.length);
    }

    /**
     * Test searchNeedsQuantity method
     */
    @Test
    public void testSearchNeedsQuantity() throws IOException {
        Need[] results = dao.searchNeedsQunatity(1);
        assertEquals(testNeeds.length, results.length);
    }

    @Test
    public void testSearchNeedsQuantityZero() throws IOException {
        Need[] results = dao.searchNeedsQunatity(0);
        assertEquals(testNeeds.length, results.length);
    }

    /**
     * Test searchNeedsByValue method
     */
    @Test
    public void testSearchNeedsValueExact() throws IOException {
        Need[] results = dao.searchNeedsValue(10.0);
        assertEquals(1, results.length);
        assertEquals("BreadBoard", results[0].getName());
    }

    @Test
    public void testSearchNeedsValuesNoMatch() throws IOException {
        Need[] results = dao.searchNeedsValue(999.0);
        assertEquals(0, results.length);
    }
}
