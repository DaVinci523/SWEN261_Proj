package com.ufund.api.ufundapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.controller.NeedsCupboardController;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.NeedsCupboardDAO;

@ExtendWith(MockitoExtension.class)
class UfundApiApplicationTests {
	private NeedsCupboardController needsCupboardController;

	@Mock
    private NeedsCupboardDAO mockNeedsCupboardDAO;

	/**
	 * Creates a new NeedsCupboardController object before each test and injects
	 * a mock NeedsCupboardDAO
	 */
	@BeforeEach
	public void setupNeedsCupboardController() {
		mockNeedsCupboardDAO = mock(NeedsCupboardDAO.class);
		needsCupboardController = new NeedsCupboardController(mockNeedsCupboardDAO);
	}
	@Test
	void contextLoads() {
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

        // When createdNeed is called on the Mock Needs Cupboard DAO, throw an IOException
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
        // When deleteAll is called on the Mock Needs Cupboard DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedsCupboardDAO).deleteAll();

        // Invoke
        ResponseEntity<Need> response = needsCupboardController.deleteAll();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
