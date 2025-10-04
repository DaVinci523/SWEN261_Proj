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
        testNeeds[0] = new Need(1, "Lab Kit", 20.00, 1, "Kit");
        testNeeds[1] = new Need(2, "Breadboard", 30.00, 2, "Board");
        testNeeds[2] = new Need(3, "Microscope", 40.00, 3, "Scope");

        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), Need[].class))
                    .thenReturn(testNeeds);

        needsCupboardFileDAO = new NeedsCupboardFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testUpdateNeed() throws IOException {
        // Setup
        Need need = new Need(3, "Oscilloscope", 50.00, 3, "Scope");

        // Invoke
        Need result = assertDoesNotThrow(() -> needsCupboardFileDAO.updateNeed(need),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = needsCupboardFileDAO.getNeed(need.getId());
        assertEquals(actual,need);
    }

    @Test
    public void testUpdateNeedNotFound() {
        // Setup
        Need need = new Need(99, "Oscilloscope", 50.00, 3, "Scope");

        // Invoke
        Need result = assertDoesNotThrow(() -> needsCupboardFileDAO.updateNeed(need),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testDeleteNeedName() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteNeedName("scope"),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test heroes array - 1 (because of the delete)
        // Because heroes attribute of HeroFileDAO is package private
        // we can access it directly
        assertEquals(needsCupboardFileDAO.needs.size(),testNeeds.length-1);
    }

    @Test 
   public void testDeleteNeedNameNotFound() {
            
        // Invoke
        boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteNeedName("wires"),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        assertEquals(needsCupboardFileDAO.needs.size(),testNeeds.length); // check for no change in length
   }  

}
