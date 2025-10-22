package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.NeedsCupboardDAO;
import com.ufund.api.ufundapi.service.NeedsCupboardService;
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
    private NeedsCupboardService mockNeedsCupboardService;

    /**
     * Before each test, create a new NeedsCupboardController object and inject
     * a mock NeedsCupboardDAO
     */
    @BeforeEach
    public void setupNeedsCupboardController() {
        mockNeedsCupboardService = mock(NeedsCupboardService.class);
        needsCupboardController = new NeedsCupboardController(mockNeedsCupboardService);
    }

    // getNeeds() Tests
    @Test
    public void testGetNeeds() throws IOException { // getHeroes may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(1, "Wires", 20.00, 5, "cord");
        needs[1] = new Need(2, "Breadboard", 10, 1, "board");

        // When getNeeds is called return the needs created above
        when(mockNeedsCupboardService.getAllNeeds()).thenReturn(needs);

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
        doThrow(new IOException()).when(mockNeedsCupboardService).getAllNeeds();

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
        when(mockNeedsCupboardService.searchNeeds(searchString)).thenReturn(needs);

        ResponseEntity<Need[]> response = needsCupboardController.searchNeeds(searchString);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());

    }
    
    @Test
    public void testSearchNeedsHandleException() throws IOException {
        String searchString = "bread";
        //when searchNeeds is called, IOException is thrown
        doThrow(new IOException()).when(mockNeedsCupboardService).searchNeeds(searchString);
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
        when(mockNeedsCupboardService.deleteNeedByType(needType)).thenReturn(true);

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
        when(mockNeedsCupboardService.deleteNeedByType(needType)).thenReturn(false);

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
        doThrow(new IOException()).when(mockNeedsCupboardService).deleteNeedByType(needType);

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
        when(mockNeedsCupboardService.getNeedById(need.getId())).thenReturn(need);

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
        when(mockNeedsCupboardService.getNeedById(needId)).thenReturn(null);

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
        doThrow(new IOException()).when(mockNeedsCupboardService).getNeedById(needId);

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
        when(mockNeedsCupboardService.deleteNeedById(needId)).thenReturn(true);

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
        when(mockNeedsCupboardService.deleteNeedById(needId)).thenReturn(false);

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
        doThrow(new IOException()).when(mockNeedsCupboardService).deleteNeedById(needId);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteNeedId(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /**
     * createNeed() Tests
     */
    @Test
    public void testCreateNeed() throws IOException {
        //Setup
        Need newNeed = new Need(101, "Battery", 12.0, 2, "power");
        when(mockNeedsCupboardService.createNeed(newNeed)).thenReturn(newNeed);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.createNeed(newNeed);

        //Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newNeed, response.getBody());
    }

    @Test
    public void testCreateNeedConflict() throws IOException {
        //Setup
        Need newNeed = new Need(101, "Battery", 12.0, 2, "power");
        when(mockNeedsCupboardService.createNeed(newNeed)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.createNeed(newNeed);

        //Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateNeedHandleException() throws IOException {
        //Setup
        Need newNeed = new Need(101, "Battery", 12.0, 2, "power");
        doThrow(new IOException()).when(mockNeedsCupboardService).createNeed(newNeed);

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.createNeed(newNeed);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * updateNeed tests
     */
    @Test
    public void testUpdateNeed() throws IOException {
        //Setup
        Need updatedNeed = new Need(101, "Battery", 12.0, 2, "power");
        when(mockNeedsCupboardService.updateNeed(updatedNeed)).thenReturn(updatedNeed);

        //Invoke
        ResponseEntity<Need> response = needsCupboardController.updateNeed(updatedNeed);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedNeed, response.getBody());
    }

    @Test
    public void testUpdateNeedNotFound() throws IOException {
        //Setup
        Need updatedNeed = new Need(101, "Battery", 12.0, 2, "power");
        when(mockNeedsCupboardService.updateNeed(updatedNeed)).thenReturn(null);

        //Invoke
        ResponseEntity<Need> response = needsCupboardController.updateNeed(updatedNeed);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateNeedHandleException() throws IOException {
        //Setup
        Need updatedNeed = new Need(101, "Battery", 12.0, 2, "power");
        doThrow(new IOException()).when(mockNeedsCupboardService).updateNeed(updatedNeed);

        //Invoke
        ResponseEntity<Need> response = needsCupboardController.updateNeed(updatedNeed);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * deleteAll tests
     */
    @Test
    public void testDeleteAll() throws IOException {
        // when deleteAllNeeds is called return true
        when(mockNeedsCupboardService.deleteAllNeeds()).thenReturn(true);

        //Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteAll();

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteAllNotFound() throws IOException {
        // simulate no needs to delete and return false
        when(mockNeedsCupboardService.deleteAllNeeds()).thenReturn(false);

        //Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteAll();

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteAllHandleException() throws IOException {
        // when deleteAllNeeds is called return true
        doThrow(new IOException()).when(mockNeedsCupboardService).deleteAllNeeds();

        //Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteAll();

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}