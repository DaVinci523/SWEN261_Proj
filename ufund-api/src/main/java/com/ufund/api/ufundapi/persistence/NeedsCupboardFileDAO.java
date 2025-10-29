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

/**
 * Implements the functionality for JSON file-based peristance for Needs
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty, Modified by CTRL Team
 */
@Component 
public class NeedsCupboardFileDAO implements NeedsCupboardDAO {
    private static final Logger LOG = Logger.getLogger(NeedsCupboardDAO.class.getName());
    Map<Integer,Need> needs;
    private ObjectMapper objectMapper;
    private static int nextId;
    private String filename;
    
    /**
     *Create Needs Cupboard DAO(Data Access Object)
     * @param filename Filename to rw from
     * @param objectMapper provides deserialization & serialization for JSON
     * 
     * @throws IOException when file cannot be accessed or read from
     */
     public NeedsCupboardFileDAO(@Value("${needs.file}") String filename, ObjectMapper objectMapper) throws IOException{
            this.filename = filename;
            this.objectMapper = objectMapper;
            load();  // load the needs from the file
    }

     /**
      * Update id for next need created
      *@return Next id
      */
    private synchronized static int nextId(){
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     *  Create array of Needs from tree map
     * @return Array of Needs, could be null
     */
    private Need[] getNeedsArray(){
        return getNeedsArray(null);
    }

    /**
     * Creates array of Needs from tree map for needs that contain "text"
     * if "text" is null, array will contain all needs in treemap
     * @param text, if this string is passed in only add needs contaiting to Array
     * @return Array of Needs
     */
    private Need[] getNeedsArray(String text){
        ArrayList<Need> needsArrayList = new ArrayList<>();

        for (Need need : needs.values()){
            if (text == null || need.getName().contains(text)){
                needsArrayList.add(need);
            }
        }

        Need[] needsArray = new Need[needsArrayList.size()];
        needsArrayList.toArray(needsArray);
        return needsArray;
    }

     /**
      * Saves Needs from map into file
      *@return true, if written successfully
      */
    private boolean save() throws IOException{
        Need[] needsArray = getNeedsArray();
        //use the object mapper to serialize jave objects to JSON 
        //Issues will raise an IOException
        objectMapper.writeValue(new File(filename), needsArray);
        return true;

    }
    /**
     * Load from JSON into the map
     * Increment id after all have been read in
     * @return true if read successfully
     */
    private boolean load() throws IOException{
        needs = new TreeMap<>();
        nextId = 0;
        //use object mapper to deserialize  file into needsArray
        Need[] needsArray = objectMapper.readValue(new File(filename), Need[].class);
        //add needs to needs tree map
        for (Need need : needsArray){
            needs.put(need.getId(), need);
            if (need.getId() > nextId)
                nextId = need.getId();
        }

        ++nextId;
        return true;
    }
    /**
     * Returns array of all needs in JSON
     * @return getNeedsArray
     */

    @Override
    public Need[] getNeeds() {
        synchronized(needs) {
            return getNeedsArray();
        }
    }
    /**
     * Search for needs containing text passed in
     * @param String text
     * @return getNeedsArray that contains text
     */
    @Override
    public Need[] searchNeeds(String text) throws IOException {
        synchronized(needs) {
            return getNeedsArray(text);
        }
    }

    /**
     * Returns single need with id
     * @param int id id to search for 
     * @return need if found, null if no id in needs JSON
     */
    @Override
    public Need getNeed(int id) throws IOException {
        synchronized(needs) {
            if (needs.containsKey(id))
                return needs.get(id);
            else
                return null;
        }

    }

    /**
     * Creates a new Need object
     * 
     * @param need: the input object containing the name, value, quantity, and type
     * @return the new need with a generated id
     * @throws I/O exception if an error occured while saving the new Need to storage
     */
    @Override
    public Need createdNeed(Need need) throws IOException {
        synchronized(need) {
            boolean exists = needs.values().stream().anyMatch(n -> n.getName().equalsIgnoreCase(need.getName()));
            if (exists){
                return null;
            }
            Need newNeed = new Need(nextId(),need.getName(), need.getValue(), need.getCurrentQuantity(), need.getMaxQuantity(),need.getType(), need.getVisible());
            needs.put(newNeed.getId(),newNeed);
            save();
            return newNeed;
        }
    }

    /**
     * Update need's contents, ie)name, value, quantity, type
     * @param  Need need
     * @return Need updated need
     */
    @Override
    public Need updateNeed(Need need) throws IOException {
        synchronized(needs) {
            if (needs.containsKey(need.getId()) == false)
                return null;

            needs.put(need.getId(),need);
            save();
            return need;
        }
    }

    /**
     * Delete need at specified id
     * @param int id
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    @Override
    public boolean deleteNeed(int id) throws IOException {
         synchronized(needs) {
            if (needs.containsKey(id)) {
                needs.remove(id);
                return save();
            }
            else
                return false;
        }
    }

    /**
     * Delete all needs of specific type
     * Seemed liike using a removeIf is the best way to do this 
     * since you cant iterate through a tree map and delete things without throwing error.
     * @param int id
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    @Override
    public boolean deleteNeedType(String type) throws IOException {
        synchronized(needs) {
            if(needs.entrySet().removeIf(entry -> entry.getValue().getType().equals(type))){ // remove entry if it matches the given type
                return save();
            }
            else
                return false;
     }
    }

    /**
     * Delete all needs
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    @Override
    public boolean deleteAll() throws IOException {
        synchronized(needs) {
            needs.clear();
            return save();
     }
    }

    /**
     * Delete all needs of specific name
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    @Override
    public boolean deleteNeedName(String name) throws IOException {
        synchronized(needs) {
            if(needs.entrySet().removeIf(entry -> entry.getValue().getName().contains(name))){
                return save();
            }
            else 
                return false;
     }
    }

}
