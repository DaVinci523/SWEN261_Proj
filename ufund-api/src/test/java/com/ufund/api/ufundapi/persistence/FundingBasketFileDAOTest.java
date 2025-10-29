package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.FundingBasketDataWrapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map; 

/**
 * Test the FundingBasketFileDAO class
 * 
 * @author CTRL Team
 */
@Tag("Persistence-tier")
public class FundingBasketFileDAOTest {
    FundingBasketFileDAO fundingBasketFileDAO;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupFundingBasketFileDAO() throws IOException {
        // Setup
        mockObjectMapper = mock(ObjectMapper.class);

        testNeeds = new Need[3];
        testNeeds[0] = new Need(1, "Wires", 20.00, 0, 5, "cord", true);
        testNeeds[1] = new Need(2, "Breadboard", 10, 0, 1, "board", true);
        testNeeds[2] = new Need(3, "Lab Kit", 100, 0, 3, "kit", true);

        User user1 = new User("user1", testNeeds);

        FundingBasketDataWrapper wrapper = new FundingBasketDataWrapper(Arrays.asList(user1));

        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), FundingBasketDataWrapper.class))
            .thenReturn(wrapper);
        
        fundingBasketFileDAO = new FundingBasketFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    // @Test
    // public void testGetUsers(){
    //     testNeeds = new Need[3];
    //     testNeeds[0] = new Need(1, "Wires", 20.00, 0, 5, "cord", true);
    //     testNeeds[1] = new Need(2, "Breadboard", 10, 0, 1, "board", true);
    //     testNeeds[2] = new Need(3, "Lab Kit", 100, 0, 3, "kit", true);

    //     User user1 = new User("user1", testNeeds);
    //     User[] users = new User[2];

    //     users[0] = user1;
        
    //     assertEquals(fundingBasketFileDAO.getUsers(), users);
    // }

    @Test
    public void testGetNeeds() {
        // Invoke
        Need[] needs = fundingBasketFileDAO.getNeeds("user1");
        // Analyze
        assertEquals(needs.length,testNeeds.length); // checks that both lists are of equal length
        for (int i = 0; i < testNeeds.length;++i) 
            assertEquals(needs[i],testNeeds[i]); // checks if each values in the list are equivallent
   }

   @Test 
   public void testDeleteNeedType() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> fundingBasketFileDAO.deleteNeedType("user1", "board"),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because needs attribute of FundingBasketFileDAO is package private
        // we can access it directly
        assertEquals(testNeeds.length-1, fundingBasketFileDAO.getNeeds("user1").length);
   }

    @Test
    public void testSearchNeeds() throws IOException {
        // Invoke
        Need[] needs = fundingBasketFileDAO.searchNeeds("user1", "La");
        // Analyze
        // Assert that testSearchNeeds should give back array of 1 Need
        // being just Lab Kit that meets search criteria
        assertEquals(needs.length,1);
        assertEquals(needs[0],testNeeds[2]);
     }
    
   @Test 
   public void testDeleteNeedTypeNull() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> fundingBasketFileDAO.deleteNeedType("user1", null),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because needs attribute of FundingBasketFileDAO is package private
        // we can access it directly
        assertEquals(testNeeds.length, fundingBasketFileDAO.getNeeds("user1").length);
   }    

   @Test 
   public void testDeleteNeedTypeNumbers() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> fundingBasketFileDAO.deleteNeedType("user1", "3294022"),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because needs attribute of FundingBasketFileDAO is package private
        // we can access it directly
        assertEquals(fundingBasketFileDAO.getNeeds("user1").length,testNeeds.length);
   }

   @Test 
   public void testDeleteNeedTypeSpecialCharacters() {
        // Invoke
        // Test many special characters and see if the program crashes or not. 
        boolean result = assertDoesNotThrow(() -> fundingBasketFileDAO.deleteNeedType("user1", "!@#$%^&*()-_+=|~`';:><.,/"),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because needs attribute of FundingBasketFileDAO is package private
        // we can access it directly
        assertEquals(fundingBasketFileDAO.getNeeds("user1").length,testNeeds.length);
   }

   @Test 
   public void testDeleteNeedTypeNotFound() {
            
        // Invoke
        boolean result = assertDoesNotThrow(() -> fundingBasketFileDAO.deleteNeed("user1", 999),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        assertEquals(fundingBasketFileDAO.getNeeds("user1").length,testNeeds.length); // check for no change in length
   } 
   
   @Test
    public void testGetNeed() throws IOException {
        // Invoke
        Need need = fundingBasketFileDAO.getNeed("user1", 1);

        // Analzye
        assertEquals(need,testNeeds[0]);
    }

    @Test
    public void testGetNeedNotFound() throws IOException {
        // Invoke
        Need need = fundingBasketFileDAO.getNeed("user1", 69);

        // Analzye
        assertEquals(need, null);
    }

    @Test
    public void testDeleteNeed() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> fundingBasketFileDAO.deleteNeed("user1", 1),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test heroes array - 1 (because of the delete)
        // Because heroes attribute of HeroFileDAO is package private
        // we can access it directly
        assertEquals(fundingBasketFileDAO.getNeeds("user1").length,testNeeds.length-1);
    }

    @Test
    public void testDeleteNeedNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> fundingBasketFileDAO.deleteNeed("user1", 999),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        // If nothing was deleted, the length stays the same.
        assertEquals(fundingBasketFileDAO.getNeeds("user1").length,testNeeds.length);
    }

    @Test
    public void testUpdateNeed() throws IOException {
        // Setup
        Need need = new Need(3, "Oscilloscope", 50.00, 0, 3, "Scope", true);

        // Invoke
        Need result = assertDoesNotThrow(() -> fundingBasketFileDAO.updateNeed("user1", need),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = fundingBasketFileDAO.getNeed("user1", need.getId());
        assertEquals(actual,need);
    }

    @Test
    public void testUpdateNeedNotFound() {
        // Setup
        Need need = new Need(99, "Oscilloscope", 50.00, 0, 3, "Scope", true);

        // Invoke
        Need result = assertDoesNotThrow(() -> fundingBasketFileDAO.updateNeed("user1", need),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testDeleteNeedName() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> fundingBasketFileDAO.deleteNeedName("user1", "board"),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test heroes array - 1 (because of the delete)
        // Because heroes attribute of HeroFileDAO is package private
        // we can access it directly
        assertEquals(fundingBasketFileDAO.getNeeds("user1").length,testNeeds.length-1);
    }

    @Test 
   public void testDeleteNeedNameNotFound() {
            
        // Invoke
        boolean result = assertDoesNotThrow(() -> fundingBasketFileDAO.deleteNeedName("user1", "wires"),"Unexpected exception thrown");

        // Analzye
        assertEquals(result,false);
        assertEquals(fundingBasketFileDAO.getNeeds("user1").length,testNeeds.length); // check for no change in length
   }  

   @Test
    public void testCreateNeed() {
        // Setup
        Need need = new Need(102,"Banana-Plug", 5.50, 0, 3, "plug", true);
        // Invoke
        Need result = assertDoesNotThrow(() -> fundingBasketFileDAO.createdNeed("user1", need),"Unexpected exception thrown");
        // Analyze
        assertNotNull(result);
        assertEquals(need.getName(),result.getName());
        assertEquals(need.getValue(),result.getValue());
        assertEquals(0,result.getCurrentQuantity());
        assertEquals(1,result.getMaxQuantity());
        assertEquals(need.getType(),result.getType());
        assertEquals(need.getId(), result.getId());
    }

    @Test
    public void testCreateNeedAlreadyExists() {

        Need need = new Need(1, "Wires", 20.00, 0, 5, "cord", true);
        // Invoke
        Need result = assertDoesNotThrow(() -> fundingBasketFileDAO.createdNeed("user1", need),"Unexpected exception thrown");
        // Analyze
        assertNotNull(result);
        assertEquals(need.getId(), result.getId());
        assertEquals(need.getName(), result.getName());
        assertEquals(0,result.getCurrentQuantity());
        assertEquals(6,result.getMaxQuantity());
        assertEquals(need.getValue(), result.getValue());
        assertEquals(need.getType(), result.getType());
    }


    @Test
    public void testDeleteAllSuccess() {
        // Invoke
        Boolean result = assertDoesNotThrow(() -> fundingBasketFileDAO.deleteAll("user1"), "Unexpected exception thrown");

        // Analyze
        assertEquals(true, result);
        assertEquals(0, fundingBasketFileDAO.getNeeds("user1").length);
    }

    @Test
    public void testDeleteAllWhenAlreadyEmpty() {
        // Setup
        assertDoesNotThrow(() -> fundingBasketFileDAO.deleteAll("user1"),
        "Unexpected exception thrown");

        // Invoke
        boolean result = assertDoesNotThrow(() -> fundingBasketFileDAO.deleteAll("user1"), "Unexpected exception thrown");

        // Analyze
        assertEquals(true,result);
        assertEquals(0, fundingBasketFileDAO.getNeeds("user1").length);
    }

}