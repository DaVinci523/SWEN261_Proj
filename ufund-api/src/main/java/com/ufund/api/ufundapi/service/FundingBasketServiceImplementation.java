package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.FundingBasketDAO;

/**
 * Implementation of the FundingBasketService interface
 * 
 * Author: Anthony Pelto
 */
@Service
public class FundingBasketServiceImplementation implements FundingBasketService {
    private final FundingBasketDAO fundingBasketDAO;
    
    /**
     * Constructs a FundingBasketServiceImplementation instance
     * 
     * @param fundingBasketDAO the data access object for the Funding Basket operations
     */
    @Autowired
    public FundingBasketServiceImplementation(FundingBasketDAO fundingBasketDAO){
        this.fundingBasketDAO = fundingBasketDAO;
    }

    /**
     * Returns array of all users
     * @return getNeedsArray
     */
    @Override
    public User[] getUsers() throws IOException {
        return fundingBasketDAO.getUsers();
    }

    /**
     * Retrieves all Needs from the funding basket
     * 
     * @return a List of Need objects
     * @throws IOException if an error occurs
     */
    @Override
    public Need[] getNeeds(String userName) throws IOException {
        return fundingBasketDAO.getNeeds(userName);
    }

    /**
     * Retrieves a single Need by its ID
     * 
     * @param id the unique identifier of the Need
     * @return the matching Need or null if it is not found
     * @throws IOException if an error occurs 
     */
    @Override
    public Need getNeed(String userName, int id) throws IOException {
        return fundingBasketDAO.getNeed(userName, id);
    }

    /**
     * Searches for Needs that contain the name specified
     * 
     * @param name a substring to match Need names
     * @return List of Needs that match the name
     * @throws IOException if an error occurs
     */
    @Override
    public Need[] searchNeeds(String userName, String name) throws IOException {
        return fundingBasketDAO.searchNeeds(userName, name);
    }

    /**
     * Creates a new Need in the funding basket
     * 
     * @param need the Need object to create
     * @return the newly created Need
     * @throws IOException if an error occurs
     */
    @Override
    public Need createdNeed(String userName, Need need) throws IOException {
        return fundingBasketDAO.createdNeed(userName, need);
    }

    /**
     * Updates an existing Need in the funding basket
     * 
     * @param need the updated Need object
     * @return the updated Need or null if the Need does not exist
     * @throws IOException if an error occurs
     */
    @Override
    public Need updateNeed(String userName, Need need) throws IOException {
        return fundingBasketDAO.updateNeed(userName, need);
    }

    /**
     * Deletes a Need from the funding basket by its ID
     * 
     * @param id the ID of the Need to delete
     * @return true if the deletion was successful
     * @throws IOException if an error occurs
     */
    @Override
    public boolean deleteNeed(String userName, int id) throws IOException {
        return fundingBasketDAO.deleteNeed(userName, id);
    }

     /**
     * Deletes all Needs of a specified type
     * 
     * @param type the type of Need to delete
     * @return true if deletion succeeded
     * @throws IOException if an error occurs
     */
    @Override
    public boolean deleteNeedType(String userName, String type) throws IOException {
        return fundingBasketDAO.deleteNeedType(userName, type);
    }

    /**
     * Deletes all Needs from the funding basket
     * 
     * @return true if the deletion was successful
     * @throws IOException if an error occurs
     */
    @Override
    public boolean deleteAll(String userName) throws IOException {
        return fundingBasketDAO.deleteAll(userName);
    }

    /**
     * Deletes Needs whose names contain a substring
     * 
     * @param name the substring to match
     * @return true if the deletion was successful
     * @throws IOException if an error occurs while saving data
     */
    @Override
    public boolean deleteNeedName(String userName, String name) throws IOException {
        return fundingBasketDAO.deleteNeedName(userName, name);
    }
}
