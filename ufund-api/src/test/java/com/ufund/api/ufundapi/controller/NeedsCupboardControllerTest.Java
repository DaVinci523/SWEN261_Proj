package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufund-api.persistence.NeedsCupboardDAO;
import com.ufund.api.ufundapi.persistence.NeedsCupboardDAO;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Need Controller class
 * 
 * @author CTRL Team
 */
@Tag("Controller-tier")
public class NeedsCupboardControllerTest {
    private NeedsCupboardController needsCupboardController;
    private NeedsCupboardDAO mockNeedsCupboardDAO;

    /**
     * Before each test, create a new NeedsCupboardController object and inject
     * a mock NeedsCupboardDAO
     */
    @BeforeEach
    public void setupHeroController() {
        mockNeedsCupboardDAO = mock(NeedsCupboardDAO.class);
        needsCupboardController = new NeedsCupboardController(mockNeedsCupboardDAO);
    }

    // getNeeds() Tests
    @Test
    public void testGetNeeds() throws IOException { // getHeroes may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(1, "Wires", 20.00, 5, "cord");
        needs[1] = new Need(2, "Breadboard", 10, 1, "board");

        // When getNeeds is called return the needs created above
        when(mockNeedsCupboardDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needsCupboardController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(needs, response.getBody());
    }

    @Test
    public void testGetNeedsHandleException() throws IOException { // getHeroes may throw IOException
        // Setup
        // When getNeeds is called on the Mock NeedsCupboardDAO, throw an IOException
        doThrow(new IOException()).when(mockNeedsCupboardDAO).getNeeds();

        // Invoke
        ResponseEntity<Need[]> response = needsCupboardController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    // deleteNeedType() Tests
    @Test
    public void testDeleteNeedType() throws IOException { // deleteNeedType may throw IOException
        // Setup
        String needType = "board";
        // when deleteNeedType is called return true, simulating successful deletion
        when(mockNeedsCupboardDAO.deleteNeedType(needType)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteNeedType(needType);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedTypeNotFound() throws IOException { // deleteNeedType may throw IOException
        // Setup
        String needType = "board";
        // when deleteNeedType is called return false, simulating failed deletion
        when(mockNeedsCupboardDAO.deleteNeedType(needType)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteNeedType(needType);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedTypeHandleException() throws IOException { // deleteNeedType may throw IOException
        // Setup
        String needType = "board";
        // When deleteNeedType is called on the Mock NeedsCupboardDAO, throw an IOException
        doThrow(new IOException()).when(mockNeedsCupboardDAO).deleteNeedType(needType);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteNeedType(needType);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}