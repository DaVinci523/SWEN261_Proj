package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.Need;

@Component 
public class NeedsCupboardFileDAO implements NeedsCupboardDAO {
    public static Logger LOG = Logger.getLogger(NeedsCupboardDAO.class.getName());
    Map<Integer,Need> needs;
    private ObjectMapper objectMapper;
    private static int nextId;
    private String fn;

    /**
     * Need to add constructor
     */
    
     /**
      * Add nextId
      */


    /**
     * Add getNeedsArray
     */

     /**
      * Add Save & Load methods for loading in needs from JSON
      */
    @Override
    public Need[] getNeeds() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNeeds'");
    }

    @Override
    public Need[] searchNeeds() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchNeeds'");
    }

    @Override
    public Need getNeed(int id) throws IOException {
        // TODO Auto-generated method stub
        //blah
        throw new UnsupportedOperationException("Unimplemented method 'getNeed'");
    }

    @Override
    public Need createdNeed(Need need) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createdNeed'");
    }

    @Override
    public Need updateNeed(Need need) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNeed'");
    }

    @Override
    public boolean deleteNeed(int id) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteNeed'");
    }
    

}
