package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.NeedsCupboardDAO;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Ufund Controller class
 */
@Tag("Controller-tier")
public class NeedsCupboardControllerTest {
    private NeedsCupboardController needController;
    private NeedsCupboardDAO mockNeedsCupboardDAO;

    /**
     * Before each test, create a new Controller object and inject
     * a mock  NeedsCupboardDAO
     */
    @BeforeEach
    public void setupController() {
        mockNeedsCupboardDAO = mock(NeedsCupboardDAO.class);
        needController = new NeedsCupboardController(mockNeedsCupboardDAO);
    }

    @Test
    public void testUpdateNeed() throws IOException { // updateHero may throw IOException
        // Setup
        Need need = new Need(1, "Lab Kit", 20.0, 0, "Kit");
        // when updateHero is called, return true simulating successful
        // update and save
        when(mockNeedsCupboardDAO.updateNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = needController.updateNeed(need);
        need.setName("Microscope");

        // Invoke
        response = needController.updateNeed(need);
        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testUpdateNeedFailed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(99, "Lab Kit", 20.0, 0, "Kit");
        // when updateHero is called, return true simulating successful
        // update and save
        when(mockNeedsCupboardDAO.updateNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateHeroHandleException() throws IOException { // updateHero may throw IOException
        // Setup
        Need need = new Need(99, "Lab Kit", 20.0, 0, "Kit");
        // When updateHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedsCupboardDAO).updateNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
    
    @Test
    public void testDeleteNeedName() throws IOException { // deleteHero may throw IOException
        // when deleteHero is called return true, simulating successful deletion
        when(mockNeedsCupboardDAO.deleteNeedName("Kit")).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeedName("Kit");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNameNotFound() throws IOException { // deleteHero may throw IOException
        // when deleteHero is called return false, simulating failed deletion
        when(mockNeedsCupboardDAO.deleteNeedName("Kit")).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeedName("Kit");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNameHandleException() throws IOException { // deleteHero may throw IOException
        // When deleteHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedsCupboardDAO).deleteNeedName("Kit");

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeedName("Kit");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

}