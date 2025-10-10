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

    //searchNeeds()     Tests
    @Test
    public void testSearchNeeds() throws IOException{
        String searchString = "La";
        Need[] needs = new Need[3];
        needs[0] = new Need(1, "Lab Kit", 20.00, 5, "kit");
        needs[2] = new Need(3, "Lab Extinguisher", 150.00, 1, "safety");
        //When search heres is classed with "La" return the two Needs created above
        when(mockNeedsCupboardDAO.searchNeeds(searchString)).thenReturn(needs);

        ResponseEntity<Need[]> response = needsCupboardController.searchNeeds(searchString);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());

    }
    
    @Test
    public void testSearchNeedsHandleException() throws IOException {
        String searchString = "bread";
        //when searchNeeds is called, IOException is thrown
        doThrow(new IOException()).when(mockNeedsCupboardDAO).searchNeeds(searchString);
        //begin
        ResponseEntity<Need[]> response = needsCupboardController.searchNeeds(searchString);
        //Test
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

    @Test
    public void testGetNeed() throws IOException {  // getHero may throw IOException
        // Setup
        Need need = new Need(99, "Silicon Wafer", 800, 5, "Electronics");
        // When the same id is passed in, our mock Hero DAO will return the Hero object
        when(mockNeedsCupboardDAO.getNeed(need.getId())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.getNeed(need.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testGetNeedNotFound() throws Exception { // createHero may throw IOException
        // Setup
        int needId = 99;
        // When the same id is passed in, our mock Hero DAO will return null, simulating
        // no hero found
        when(mockNeedsCupboardDAO.getNeed(needId)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetNeedHandleException() throws Exception { // createHero may throw IOException
        // Setup
        int needId = 99;
        // When getHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedsCupboardDAO).getNeed(needId);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    
    @Test
    public void testDeleteNeed() throws IOException { // deleteHero may throw IOException
        // Setup
        int needId = 99;
        // when deleteHero is called return true, simulating successful deletion
        when(mockNeedsCupboardDAO.deleteNeed(needId)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteNeedId(needId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { // deleteHero may throw IOException
        // Setup
        int needId = 99;
        // when deleteHero is called return false, simulating failed deletion
        when(mockNeedsCupboardDAO.deleteNeed(needId)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteNeedId(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException { // deleteHero may throw IOException
        // Setup
        int needId = 99;
        // When deleteHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedsCupboardDAO).deleteNeed(needId);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteNeedId(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    
    @Test
    public void testUpdateNeed() throws IOException { // updateHero may throw IOException
        // Setup
        Need need = new Need(1, "Lab Kit", 20.0, 0, "Kit");
        // when updateHero is called, return true simulating successful
        // update and save
        when(mockNeedsCupboardDAO.updateNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = needsCupboardController.updateNeed(need);
        need.setName("Microscope");

        // Invoke
        response = needsCupboardController.updateNeed(need);
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
        ResponseEntity<Need> response = needsCupboardController.updateNeed(need);

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
        ResponseEntity<Need> response = needsCupboardController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
    
    @Test
    public void testDeleteNeedName() throws IOException { // deleteHero may throw IOException
        // when deleteHero is called return true, simulating successful deletion
        when(mockNeedsCupboardDAO.deleteNeedName("Kit")).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteNeedName("Kit");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNameNotFound() throws IOException { // deleteHero may throw IOException
        // when deleteHero is called return false, simulating failed deletion
        when(mockNeedsCupboardDAO.deleteNeedName("Kit")).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteNeedName("Kit");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNameHandleException() throws IOException { // deleteHero may throw IOException
        // When deleteHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedsCupboardDAO).deleteNeedName("Kit");

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteNeedName("Kit");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateNeed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(67,"Breadboard", 25.30, 2, "board");
        // when createdNeed is called, return true simulating successful
        // creation and save
        when(mockNeedsCupboardDAO.createdNeed(org.mockito.Mockito.any(Need.class))).thenReturn(need);


        // Invoke
        ResponseEntity<Need> response = needsCupboardController.createNeed(need);


        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(need,response.getBody());
    }


    @Test
    public void testCreateNeedFailed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(67,"Wire", 9.82, 5, "cord");
        // when createdNeed is called, return false simulating failed
        // creation and save
        when(mockNeedsCupboardDAO.createdNeed(org.mockito.Mockito.any(Need.class))).thenReturn(null);


        // Invoke
        ResponseEntity<Need> response = needsCupboardController.createNeed(need);


        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }


    @Test
    public void testCreateNeedHandleException() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(67,"Microscope", 299.99, 1, "scope");


        // When createdNeed is called on the Mock FundingBasketDAO, throw an IOException
        when(mockNeedsCupboardDAO.createdNeed(org.mockito.Mockito.any(Need.class))).thenThrow(new IOException());


        // Invoke
        ResponseEntity<Need> response = needsCupboardController.createNeed(need);


        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }



    @Test
    public void testDeleteAll() throws IOException { // deleteAll may throw IOException
        // when deleteAll is called return true, simulating successful deletion
        when(mockNeedsCupboardDAO.deleteAll()).thenReturn(true);


        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteAll();


        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }


    @Test
    public void testDeleteAllNotFound() throws IOException { // deleteAll may throw IOException
        // when deleteAll is called return false, simulating failed deletion
        when(mockNeedsCupboardDAO.deleteAll()).thenReturn(false);


        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteAll();


        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }


    @Test
    public void testDeleteAllHandleException() throws IOException { // deleteAll may throw IOException
        // When deleteAll is called on the Mock FundingBasket DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedsCupboardDAO).deleteAll();


        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteAll();


        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}