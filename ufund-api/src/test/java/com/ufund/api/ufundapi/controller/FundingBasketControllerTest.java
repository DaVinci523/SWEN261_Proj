package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.FundingBasketDAO;
import com.ufund.api.ufundapi.service.FundingBasketService;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

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
    private FundingBasketService mockfundingBasketService;

    private static final String USERNAME = "user";

    /**
     * Before each test, create a new FundingBasketController object and inject
     * a mock FundingBasketDAO
     */
    @BeforeEach
    public void setupNeedController() {
        mockfundingBasketService = mock(FundingBasketService.class);
        fundingBasketController = new FundingBasketController(mockfundingBasketService);
    }

    // getUsers() Tests
    @Test
    public void testGetUsers() throws IOException { // getUsers may throw IOException
        // Setup
        User[] users = new User[2];
        
        Need[] usrNeeds = {new Need(1, "Wires", 20.00, 0, 5, "cord", true)};

        users[0] = new User("helper123", usrNeeds);
        users[1] = new User("helper456", usrNeeds);

        // When getNeeds is called return the needs created above
        when(mockfundingBasketService.getUsers()).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = fundingBasketController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetUsersFalse() throws IOException { // getUsers may throw IOException
        // Setup

        // When getNeeds is called return the needs created above
        when(mockfundingBasketService.getUsers()).thenReturn(null);

        // Invoke
        ResponseEntity<User[]> response = fundingBasketController.getUsers();

        // Analyze
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // getNeeds() Tests
    @Test
    public void testGetNeeds() throws IOException { // getNeeds may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(1, "Wires", 20.00, 0, 5, "cord", true);
        needs[1] = new Need(2, "Breadboard", 10, 0, 1, "board", true);

        // When getNeeds is called return the needs created above
        when(mockfundingBasketService.getNeeds(USERNAME)).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = fundingBasketController.getNeeds(USERNAME);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(needs, response.getBody());
    }

    @Test
    public void testGetNeedsHandleException() throws IOException { // getNeeds may throw IOException
        // Setup
        // When getNeeds is called on the Mock FundingBasketDAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketService).getNeeds(USERNAME);

        // Invoke
        ResponseEntity<Need[]> response = fundingBasketController.getNeeds(USERNAME);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetNeedsNull() throws IOException {
        // Setup
        // When getNeeds is called and returns null, simulating no needs found
        when(mockfundingBasketService.getNeeds(USERNAME)).thenReturn(null);

        // Invoke
        ResponseEntity<Need[]> response = fundingBasketController.getNeeds(USERNAME);

        // Analyze
        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }
    
    //searchNeeds()     Tests
    @Test
    public void testSearchNeeds() throws IOException{
        String searchString = "La";
        Need[] needs = new Need[3];
        needs[0] = new Need(1, "Lab Kit", 20.00, 0, 5, "kit", true);
        needs[2] = new Need(3, "Lab Extinguisher", 150.00, 0, 1, "safety", true);
        //When search heres is classed with "La" return the two Needs created above
        when(mockfundingBasketService.searchNeeds(USERNAME, searchString)).thenReturn(needs);

        ResponseEntity<Need[]> response = fundingBasketController.searchNeeds(USERNAME, searchString);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());

    }
    
    @Test
    public void testSearchNeedsHandleException() throws IOException {
        String searchString = "bread";
        //when searchNeeds is called, IOException is thrown
        doThrow(new IOException()).when(mockfundingBasketService).searchNeeds(USERNAME, searchString);
        //begin
        ResponseEntity<Need[]> response = fundingBasketController.searchNeeds(USERNAME, searchString);
        //Test
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    // deleteNeedType() Tests
    @Test
    public void testDeleteNeedType() throws IOException { // deleteNeedType may throw IOException
        // Setup
        String needType = "board";
        // when deleteNeedType is called return true, simulating successful deletion
        when(mockfundingBasketService.deleteNeedType(USERNAME, needType)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedType(USERNAME, needType);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedTypeNotFound() throws IOException { // deleteNeedType may throw IOException
        // Setup
        String needType = "board";
        // when deleteNeedType is called return false, simulating failed deletion
        when(mockfundingBasketService.deleteNeedType(USERNAME, needType)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedType(USERNAME, needType);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedTypeHandleException() throws IOException { // deleteNeedType may throw IOException
        // Setup
        String needType = "board";
        // When deleteNeedType is called on the Mock FundingBasketDAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketService).deleteNeedType(USERNAME, needType);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedType(USERNAME, needType);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNeed() throws IOException {  // getNeed may throw IOException
        // Setup
        Need need = new Need(99, "Silicon Wafer", 800, 0, 5, "Electronics", true);
        // When the same id is passed in, our mock Need DAO will return the Need object
        when(mockfundingBasketService.getNeed(USERNAME, need.getId())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.getNeed(USERNAME, need.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testGetNeedNotFound() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 99;
        // When the same id is passed in, our mock Need DAO will return null, simulating
        // no Need found
        when(mockfundingBasketService.getNeed(USERNAME, needId)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.getNeed(USERNAME, needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetNeedHandleException() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 99;
        // When getNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketService).getNeed(USERNAME, needId);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.getNeed(USERNAME, needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    
    @Test
    public void testDeleteNeed() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 1;
        // when deleteNeed is called return true, simulating successful deletion
        when(mockfundingBasketService.deleteNeed(USERNAME, needId)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedId(USERNAME, needId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 1;
        // when deleteNeed is called return false, simulating failed deletion
        when(mockfundingBasketService.deleteNeed(USERNAME, needId)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedId(USERNAME, needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 1;
        // When deleteNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketService).deleteNeed(USERNAME, needId);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedId(USERNAME, needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateNeed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(1, "Lab Kit", 20.0, 0, 0, "Kit", true);
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockfundingBasketService.updateNeed(USERNAME, need)).thenReturn(need);
        ResponseEntity<Need> response = fundingBasketController.updateNeed(USERNAME, need);
        need.setName("Microscope");

        // Invoke
        response = fundingBasketController.updateNeed(USERNAME, need);
        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testUpdateNeedFailed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(99, "Lab Kit", 20.0, 0, 0, "Kit", true);
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockfundingBasketService.updateNeed(USERNAME, need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.updateNeed(USERNAME, need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateNeedHandleException() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(99, "Lab Kit", 20.0, 0, 0, "Kit", true);
        // When updateNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketService).updateNeed(USERNAME, need);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.updateNeed(USERNAME, need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    // //updateNeedVisibility() Tests
    //  @Test
    // public void testUpdateNeedVisibility() throws IOException { // updateNeed may throw IOException
    //     // Setup
    //     Need need = new Need(1, "Lab Kit", 20.0, 0, 0, "Kit", true);
    //     // when updateNeed is called, return true simulating successful
    //     // update and save
    //     when(fundingBasketController.updateNeedVisibility(USERNAME, need.getId(), false)).thenReturn(new Need(1, "Lab Kit", 20.0, 0, 0, "Kit", false));
    //     ResponseEntity<Need> response = fundingBasketController.updateNeedVisibility(USERNAME, need.getId(), false);

    //     // Invoke
    //     response = fundingBasketController.updateNeedVisibility(USERNAME, need.getId(), false);
    //     // Analyze
    //     assertEquals(HttpStatus.OK,response.getStatusCode());
    //     assertEquals(need,response.getBody());
    // }

    // @Test
    // public void testUpdateNeedVisibilityFailed() throws IOException { // updateNeed may throw IOException
    //     // Setup
    //     Need need = new Need(99, "Lab Kit", 20.0, 0, 0, "Kit", true);
    //     // when updateNeed is called, return true simulating successful
    //     // update and save
    //     when(fundingBasketController.updateNeedVisibility(USERNAME, need.getId(), true)).thenReturn(null);

    //     // Invoke
    //     ResponseEntity<Need> response = fundingBasketController.updateNeedVisibility(USERNAME, need.getId(), true);

    //     // Analyze
    //     assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    // }

    // @Test
    // public void testUpdateNeedVisibilityHandleException() throws IOException { // updateNeed may throw IOException
    //     // Setup
    //     Need need = new Need(99, "Lab Kit", 20.0, 0, 0, "Kit", true);
    //     // When updateNeed is called on the Mock Need DAO, throw an IOException
    //     doThrow(new IOException()).when(fundingBasketController).updateNeedVisibility(USERNAME, need.getId(), true);

    //     // Invoke
    //     ResponseEntity<Need> response = fundingBasketController.updateNeedVisibility(USERNAME, need.getId(), true);

    //     // Analyze
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    // }
    
    @Test
    public void testDeleteNeedName() throws IOException { // deleteNeed may throw IOException
        // when deleteNeed is called return true, simulating successful deletion
        when(mockfundingBasketService.deleteNeedName(USERNAME, "Kit")).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedName(USERNAME, "Kit");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNameNotFound() throws IOException { // deleteNeed may throw IOException
        // when deleteNeed is called return false, simulating failed deletion
        when(mockfundingBasketService.deleteNeedName(USERNAME, "Kit")).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedName(USERNAME, "Kit");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNameHandleException() throws IOException { // deleteNeed may throw IOException
        // When deleteNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketService).deleteNeedName(USERNAME, "Kit");

        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteNeedName(USERNAME, "Kit");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateNeed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(67,"Breadboard", 25.30, 0, 2, "board", true);
        // when createdNeed is called, return true simulating successful
        // creation and save
        when(mockfundingBasketService.createdNeed(eq(USERNAME), org.mockito.Mockito.any(Need.class))).thenReturn(need);


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.createNeed(USERNAME, need);


        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(need,response.getBody());
    }


    @Test
    public void testCreateNeedFailed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(67,"Wire", 9.82, 0, 5, "cord", true);
        // when createdNeed is called, return false simulating failed
        // creation and save
        when(mockfundingBasketService.createdNeed(eq(USERNAME), org.mockito.Mockito.any(Need.class))).thenReturn(null);


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.createNeed(USERNAME, need);


        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }


    @Test
    public void testCreateNeedHandleException() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(67,"Microscope", 299.99, 0, 1, "scope", true);


        // When createdNeed is called on the Mock FundingBasketDAO, throw an IOException
        when(mockfundingBasketService.createdNeed(eq(USERNAME), org.mockito.Mockito.any(Need.class))).thenThrow(new IOException());


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.createNeed(USERNAME, need);


        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }



    @Test
    public void testDeleteAll() throws IOException { // deleteAll may throw IOException
        // when deleteAll is called return true, simulating successful deletion
        when(mockfundingBasketService.deleteAll(USERNAME)).thenReturn(true);


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteAll(USERNAME);


        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }


    @Test
    public void testDeleteAllNotFound() throws IOException { // deleteAll may throw IOException
        // when deleteAll is called return false, simulating failed deletion
        when(mockfundingBasketService.deleteAll(USERNAME)).thenReturn(false);


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteAll(USERNAME);


        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }


    @Test
    public void testDeleteAllHandleException() throws IOException { // deleteAll may throw IOException
        // When deleteAll is called on the Mock FundingBasket DAO, throw an IOException
        doThrow(new IOException()).when(mockfundingBasketService).deleteAll(USERNAME);


        // Invoke
        ResponseEntity<Need> response = fundingBasketController.deleteAll(USERNAME);


        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

}