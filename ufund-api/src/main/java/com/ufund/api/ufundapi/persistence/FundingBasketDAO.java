package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

/**
 * Interfacing for Need and User objects
 * @author CTRL Team
 */
public interface FundingBasketDAO {
   
    /**
     * Returns array of all users
     * @return getNeedsArray
     */
    User[] getUsers() throws IOException;
    
    /**
     * Returns array of all needs for the specified user
     * @param userName the user this method will modify
     * @return getNeedsArray
     */
    Need[] getNeeds(String userName) throws IOException;
    
    /**
     * Search for needs containing text passed in
     * @param userName the user this method will search in
     * @param String text
     * @return getNeedsArray that contains text
     */
    Need[] searchNeeds(String userName, String text) throws IOException;

    /**
     * Returns single need with id
     * @param userName the user this method will retrieve from
     * @param int id id to search for 
     * @return need if found, null if no id in needs JSON
     */
    Need getNeed(String userName, int id) throws IOException;

    /**
     * Creates a new Need object
     * 
     * @param userName: the name of the user that this need is being added to
     * @param need: the input object containing the name, value, quantity, and type
     * @return the new need with a generated id
     * @throws I/O exception if an error occured while saving the new Need to storage
     */
    Need createdNeed(String userName, Need need) throws IOException;

    /**
     * Update need's contents, ie)name, value, quantity, and type
     * @param userName the user this method will modify
     * @param  Need need
     * @return Need updated need
     */
    Need updateNeed(String userName, Need need) throws IOException;

    /**
     * Delete need at specified id
     * @param userName the user this method will modify
     * @param int id
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    boolean deleteNeed(String userName, int id) throws IOException;

    /**
     * Delete needs with the given type
     * @param userName the user this method will modify
     * @param String type
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    boolean deleteNeedType(String userName, String type) throws IOException;

    /**
     * Delete all needs
     * @param userName the user this method will modify
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    boolean deleteAll(String userName) throws IOException;

    /**
     * Deletes needs that contain string name
     * @param userName the user this method will modify
     * @param  String name
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    boolean deleteNeedName(String userName, String name) throws IOException;

}