package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ufund.api.ufundapi.model.Need;

import java.io.File;
import java.io.IOException; 

/**
 * Test the NeedsCupboardFileDAO class
 * 
 * @author CTRL Team
 */
@Tag("Persistence-tier")
public class NeedsCupboardFileDAOTest {
    NeedsCupboardFileDAO needsCupboardFileDAO;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupNeedsCupbboardFileDAO() throws IOException {
        // Setup
        mockObjectMapper = mock(ObjectMapper.class);

        testNeeds = new Need[3];
        testNeeds[0] = new Need(1, "Wires", 20.00, 5, "cord");
        testNeeds[1] = new Need(2, "Breadboard", 10, 1, "board");
        testNeeds[2] = new Need(3, "Lab Kit", 100, 3, "kit");

        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), Need[].class))
                    .thenReturn(testNeeds);

        needsCupboardFileDAO = new NeedsCupboardFileDAO("doesnt_matter.txt", mockObjectMapper);
    }
    
    @Test
    public void testGetNeeds() {
        // Tests: 
            // if all needs are returned, 
            // bad data is handled well, 
            // empty list returned when no needs are given
            
        // Invoke
        Need[] needs = needsCupboardFileDAO.getNeeds();

        // Analyze
        assertEquals(needs.length,testNeeds.length); // checks that both lists are of equal length
        for (int i = 0; i < testNeeds.length;++i) 
            assertEquals(needs[i],testNeeds[i]); // checks if each values in the list are equivallent
   }

   @Test 
   public void testDeleteNeedType() {
        // Tests:
            // if need is properly deleted
            // if need type not found
            // bad data is handled well
            
        // Invoke
        boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteNeedType("board"),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because needs attribute of NeedsCupboardFileDAO is package private
        // we can access it directly
        assertEquals(needsCupboardFileDAO.needs.size(),testNeeds.length-1);
   }    

   @Test 
   public void testDeleteNeedTypeNotFound() {
        // Tests:
            // if need is properly deleted
            // if need type not found
            // bad data is handled well
            
        // Invoke
        boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteNeedType("box"),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        assertEquals(needsCupboardFileDAO.needs.size(),testNeeds.length); // check for no change in length
   }    
}