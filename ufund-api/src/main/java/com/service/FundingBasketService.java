package com.service;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Need;

/**
 * Service interface for Funding Basket operations
 * 
 * Author: Anthony Pelto
 */
public interface FundingBasketService {
    /**
     * Retrieves all Needs from the funding basket
     * 
     * @return a List of Need objects
     * @throws IOException if an error occurs
     */
    Need[] getNeeds() throws IOException;

    /**
     * Retrieves a single Need by its ID
     * 
     * @param id the unique identifier of the Need
     * @return the matching Need or null if it is not found
     * @throws IOException if an error occurs 
     */
    Need getNeed(int id) throws IOException;

    /**
     * Searches for Needs that contain the name specified
     * 
     * @param name a substring to match Need names
     * @return List of Needs that match the name
     * @throws IOException if an error occurs
     */
    Need[] searchNeeds(String name) throws IOException;

    /**
     * Creates a new Need in the funding basket
     * 
     * @param need the Need object to create
     * @return the newly created Need
     * @throws IOException if an error occurs
     */
    Need createdNeed(Need need) throws IOException;

    /**
     * Updates an existing Need in the funding basket
     * 
     * @param need the updated Need object
     * @return the updated Need or null if the Need does not exist
     * @throws IOException if an error occurs
     */
    Need updateNeed(Need need) throws IOException;

    /**
     * Deletes a Need from the funding basket by its ID
     * 
     * @param id the ID of the Need to delete
     * @return true if the deletion was successful
     * @throws IOException if an error occurs
     */
    boolean deleteNeed(int id) throws IOException;

    /**
     * Deletes all Needs of a specified type
     * 
     * @param type the type of Need to delete
     * @return true if deletion succeeded
     * @throws IOException if an error occurs
     */
    boolean deleteNeedType(String type) throws IOException;

    /**
     * Deletes all Needs from the funding basket
     * 
     * @return true if the deletion was successful
     * @throws IOException if an error occurs
     */
    boolean deleteAll() throws IOException;

    /**
     * Deletes Needs whose names contain a substring
     * 
     * @param name the substring to match
     * @return true if the deletion was successful
     * @throws IOException if an error occurs while saving data
     */
    boolean deleteNeedName(String name) throws IOException;
} 
