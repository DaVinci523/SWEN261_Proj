package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.FundingBasketDAO;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Funding Basket Controller class
 * 
 * @author CTRL Team
 */
@Tag("Controller-tier")
public class FundingBasketControllerTest {
    private FundingBasketController fundingBasketController;
    private FundingBasketDAO mockfundingBasketDAO;

    /**
     * Before each test, create a new FundingBasketController object and inject
     * a mock FundingBasketDAO
     */
    @BeforeEach
    public void setupHeroController() {
        mockfundingBasketDAO = mock(FundingBasketDAO.class);
        fundingBasketController = new FundingBasketController(mockfundingBasketDAO);
    }

    // getNeeds() Tests
    @Test
    public void testGetNeeds() throws IOException { // getHeroes may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(1, "Wires", 20.00, 5, "cord");
        needs[1] = new Need(2, "Breadboard", 10, 1, "board");

        // When getNeeds is called return the needs created above
        when(mockfundingBasketDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = fundingBasketController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(needs, response.getBody());
    }

    @Test
    public void testGetNeedsHandleException() throws IOException { // getHeroes may throw IOException
        // Setup
        // When getNeeds is called on the Mock FundingBasketDAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketDAO).getNeeds();

        // Invoke
        ResponseEntity<Need[]> response = fundingBasketController.getNeeds();

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
        when(mockfundingBasketDAO.searchNeeds(searchString)).thenReturn(needs);

        ResponseEntity<Need[]> response = fundingBasketController.searchNeeds(searchString);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());

    }
    
    @Test
    public void testSearchNeedsHandleException() throws IOException {
        String searchString = "bread";
        //when searchNeeds is called, IOException is thrown
        doThrow(new IOException()).when(mockfundingBasketDAO).searchNeeds(searchString);
        //begin
        ResponseEntity<Need[]> response = fundingBasketController.searchNeeds(searchString);
        //Test
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    // deleteNeedType() Tests
    @Test
    public void testDeleteNeedType() throws IOException { // deleteNeedType may throw IOException
        // Setup
        String needType = "board";
        // when deleteNeedType is called return true, simulating successful deletion
        when(mockfundingBasketDAO.deleteNeedType(needType)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedType(needType);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedTypeNotFound() throws IOException { // deleteNeedType may throw IOException
        // Setup
        String needType = "board";
        // when deleteNeedType is called return false, simulating failed deletion
        when(mockfundingBasketDAO.deleteNeedType(needType)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedType(needType);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedTypeHandleException() throws IOException { // deleteNeedType may throw IOException
        // Setup
        String needType = "board";
        // When deleteNeedType is called on the Mock FundingBasketDAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketDAO).deleteNeedType(needType);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedType(needType);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNeed() throws IOException {  // getHero may throw IOException
        // Setup
        Need need = new Need(99, "Silicon Wafer", 800, 5, "Electronics");
        // When the same id is passed in, our mock Hero DAO will return the Hero object
        when(mockfundingBasketDAO.getNeed(need.getId())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.getNeed(need.getId());

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
        when(mockfundingBasketDAO.getNeed(needId)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetNeedHandleException() throws Exception { // createHero may throw IOException
        // Setup
        int needId = 99;
        // When getHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketDAO).getNeed(needId);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    
    @Test
    public void testDeleteNeed() throws IOException { // deleteHero may throw IOException
        // Setup
        int needId = 1;
        // when deleteHero is called return true, simulating successful deletion
        when(mockfundingBasketDAO.deleteNeed(needId)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedId(needId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { // deleteHero may throw IOException
        // Setup
        int needId = 1;
        // when deleteHero is called return false, simulating failed deletion
        when(mockfundingBasketDAO.deleteNeed(needId)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedId(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException { // deleteHero may throw IOException
        // Setup
        int needId = 1;
        // When deleteHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketDAO).deleteNeed(needId);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedId(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateNeed() throws IOException { // updateHero may throw IOException
        // Setup
        Need need = new Need(1, "Lab Kit", 20.0, 0, "Kit");
        // when updateHero is called, return true simulating successful
        // update and save
        when(mockfundingBasketDAO.updateNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = fundingBasketController.updateNeed(need);
        need.setName("Microscope");

        // Invoke
        response = fundingBasketController.updateNeed(need);
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
        when(mockfundingBasketDAO.updateNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateHeroHandleException() throws IOException { // updateHero may throw IOException
        // Setup
        Need need = new Need(99, "Lab Kit", 20.0, 0, "Kit");
        // When updateHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketDAO).updateNeed(need);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
    
    @Test
    public void testDeleteNeedName() throws IOException { // deleteHero may throw IOException
        // when deleteHero is called return true, simulating successful deletion
        when(mockfundingBasketDAO.deleteNeedName("Kit")).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedName("Kit");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNameNotFound() throws IOException { // deleteHero may throw IOException
        // when deleteHero is called return false, simulating failed deletion
        when(mockfundingBasketDAO.deleteNeedName("Kit")).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedName("Kit");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNameHandleException() throws IOException { // deleteHero may throw IOException
        // When deleteHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketDAO).deleteNeedName("Kit");

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedName("Kit");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateNeed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(67,"Breadboard", 25.30, 2, "board");
        // when createdNeed is called, return true simulating successful
        // creation and save
        when(mockfundingBasketDAO.createdNeed(org.mockito.Mockito.any(Need.class))).thenReturn(need);


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.createNeed(need);


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
        when(mockfundingBasketDAO.createdNeed(org.mockito.Mockito.any(Need.class))).thenReturn(null);


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.createNeed(need);


        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }


    @Test
    public void testCreateNeedHandleException() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(67,"Microscope", 299.99, 1, "scope");


        // When createdNeed is called on the Mock FundingBasketDAO, throw an IOException
        when(mockfundingBasketDAO.createdNeed(org.mockito.Mockito.any(Need.class))).thenThrow(new IOException());


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.createNeed(need);


        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }



    @Test
    public void testDeleteAll() throws IOException { // deleteAll may throw IOException
        // when deleteAll is called return true, simulating successful deletion
        when(mockfundingBasketDAO.deleteAll()).thenReturn(true);


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteAll();


        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }


    @Test
    public void testDeleteAllNotFound() throws IOException { // deleteAll may throw IOException
        // when deleteAll is called return false, simulating failed deletion
        when(mockfundingBasketDAO.deleteAll()).thenReturn(false);


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteAll();


        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }


    @Test
    public void testDeleteAllHandleException() throws IOException { // deleteAll may throw IOException
        // When deleteAll is called on the Mock FundingBasket DAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketDAO).deleteAll();


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteAll();


        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

}