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
        // Invoke
        Need[] needs = needsCupboardFileDAO.getNeeds();
        // Analyze
        assertEquals(needs.length,testNeeds.length); // checks that both lists are of equal length
        for (int i = 0; i < testNeeds.length;++i) 
            assertEquals(needs[i],testNeeds[i]); // checks if each values in the list are equivallent
   }

   @Test 
   public void testDeleteNeedType() {
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
    public void testSearchNeeds() throws IOException {
        // Invoke
        Need[] needs = needsCupboardFileDAO.searchNeeds("La");
        // Analyze
        // Assert that testSearchNeeds should give back array of 1 Need
        // being just Lab Kit that meets search criteria
        assertEquals(needs.length,1);
        assertEquals(needs[0],testNeeds[2]);
     }
    
   @Test 
   public void testDeleteNeedTypeNull() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteNeedType(null),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because needs attribute of NeedsCupboardFileDAO is package private
        // we can access it directly
        assertEquals(needsCupboardFileDAO.needs.size(),testNeeds.length);
   }    

   @Test 
   public void testDeleteNeedTypeNumbers() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteNeedType("3294022"),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because needs attribute of NeedsCupboardFileDAO is package private
        // we can access it directly
        assertEquals(needsCupboardFileDAO.needs.size(),testNeeds.length);
   }

   @Test 
   public void testDeleteNeedTypeSpecialCharacters() {
        // Invoke
        // Test many special characters and see if the program crashes or not. 
        boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteNeedType("!@#$%^&*()-_+=|~`';:><.,/"),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because needs attribute of NeedsCupboardFileDAO is package private
        // we can access it directly
        assertEquals(needsCupboardFileDAO.needs.size(),testNeeds.length);
   }

   @Test 
   public void testDeleteNeedTypeNotFound() {
            
        // Invoke
        boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteNeedType("box"),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        assertEquals(needsCupboardFileDAO.needs.size(),testNeeds.length); // check for no change in length
   } 
   
   @Test
    public void testGetNeed() throws IOException {
        // Invoke
        Need need = needsCupboardFileDAO.getNeed(1);

        // Analzye
        assertEquals(need,testNeeds[0]);
    }

    @Test
    public void testDeleteNeed() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteNeed(1),
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
        boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteNeedName("board"),
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

   @Test
    public void testCreateNeed() {
    // Setup
    Need need = new Need(102,"Banana-Plug", 5.50, 3, "plug");
    // Invoke
    Need result = assertDoesNotThrow(() -> needsCupboardFileDAO.createdNeed(need),"Unexpected exception thrown");
    // Analyze
    assertNotNull(result);
    assertEquals(need.getName(),result.getName());
    assertEquals(need.getValue(),result.getValue());
    assertEquals(need.getQuantity(),result.getQuantity());
    assertEquals(need.getType(),result.getType());
    }


@Test
public void testDeleteAllSuccess() {
	// Invoke
	Boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteAll(), "Unexpected exception thrown");

	// Analyze
	assertEquals(true, result);
	assertEquals(0, needsCupboardFileDAO.needs.size());
}

@Test
public void testDeleteAllWhenAlreadyEmpty() {
	// Setup
	needsCupboardFileDAO.needs.clear();

	// Invoke
	boolean result = assertDoesNotThrow(() -> needsCupboardFileDAO.deleteAll(), "Unexpected exception thrown");

	// Analyze
	assertEquals(true,result);
	assertEquals(0, needsCupboardFileDAO.needs.size());
}

}