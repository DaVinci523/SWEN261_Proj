package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Need;

/**
 * Interfacing for Need object
 * @author SWEN Faculty, Modified by CTRL Team
 */
public interface NeedsCupboardDAO {
   
    /**
     * Returns array of all needs in JSON
     * @return getNeedsArray
     */
    Need[] getNeeds() throws IOException;
    
    /**
     * Search for needs containing text passed in
     * @param String text
     * @return getNeedsArray that contains text
     */
    Need[] searchNeeds(String text) throws IOException;

    Need[] searchNeedsType(String text) throws IOException;
    /**
     * Returns single need with id
     * @param int id id to search for 
     * @return need if found, null if no id in needs JSON
     */
    Need getNeed(int id) throws IOException;

    /**
     * Creates a new Need object
     * 
     * @param need: the input object containing the name, value, quantity, and type
     * @return the new need with a generated id
     * @throws I/O exception if an error occured while saving the new Need to storage
     */
    Need createdNeed(Need need) throws IOException;

    /**
     * Update need's contents, ie)name, value, quantity, and type
     * @param  Need need
     * @return Need updated need
     */
    Need updateNeed(Need need) throws IOException;

    /**
     * Delete need at specified id
     * @param int id
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    boolean deleteNeed(int id) throws IOException;

    /**
     * Delete needs with the given type
     * @param String type
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    boolean deleteNeedType(String type) throws IOException;

    /**
     * Delete all needs
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    boolean deleteAll() throws IOException;

    /**
     * Deletes needs that contain string name
     * @param  String name
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    boolean deleteNeedName(String name) throws IOException;

}