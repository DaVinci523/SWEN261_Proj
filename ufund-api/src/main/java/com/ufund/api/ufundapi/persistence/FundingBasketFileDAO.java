package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.ufund.api.ufundapi.model.FundingBasketDataWrapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

/**
 * Implements the functionality for JSON file-based peristance for Needs
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty, Modified by CTRL Team
 */
@Component 
public class FundingBasketFileDAO implements FundingBasketDAO {
    private static final Logger LOG = Logger.getLogger(FundingBasketDAO.class.getName());
    Map<Integer,Need> currentUserNeeds; // a map of needs used to access the current user's Map<Integer, Need>
    Map<String,Need[]> users; // users are mapped to username:needs
    private ObjectMapper objectMapper;
    private static String currentUser;
    // private static int nextId;
    private String filename;
    
    /**
     *Create Needs Funding Basket DAO(Data Access Object)
     * @param filename Filename to rw from
     * @param objectMapper provides deserialization & serialization for JSON
     * 
     * @throws IOException when file cannot be accessed or read from
     */
     public FundingBasketFileDAO(@Value("${funding_basket.file}") String filename, ObjectMapper objectMapper) throws IOException{
            this.filename = filename;
            this.objectMapper = objectMapper;
            load();  // load the needs from the file
    }

     /**
      * Update id for next need created
      *@return Next id
    //   */
    // private synchronized static int nextId(){
    //     int id = nextId;
    //     ++nextId;
    //     return id;
    // }

    /**
     * Swaps the current user to the specified userName
     * @param userName
     * @return
     */
    private boolean swapCurrentUser(String userName){
        synchronized(currentUser){
            currentUser = userName;
            try{
                currentUserNeeds = users.get(currentUser);
            } catch (NotFound e){ // send error message if userName is not in the data
                System.out.println("Error: Not a valid userName");
                return false;
            }
        }
        return true;
    }

    /**
     *  Create array of Users from tree map
     * @return Array of Users, could be null
     */
    private User[] getUserArray(String userName){
        return getUserArray(userName, null);
    }


    /**
     * Creates array of all Users from tree map for needs 
     * if "text" is null, array will contain all needs in treemap
     * @param text, if this string is passed in only add needs contaiting to Array
     * @return Array of Needs
     */
    private User[] getUserArray(String userName, String text){
        ArrayList<User> usersArrayList = new ArrayList<>();

        for ( String user : users.keySet() ){
            if (text == null || user.contains(text)){ // check for users that contain text
                usersArrayList.add(new User(user, users.get(user))); // add user to the array list
            }
        }

        User[] usersArray = new User[usersArrayList.size()];
        usersArrayList.toArray(usersArray);
        return usersArray;
    }

    /**
     *  Create array of Needs from tree map
     * @return Array of Needs, could be null
     */
    private Need[] getNeedsArray(String userName){
        return getNeedsArray(userName, null);
    }

    /**
     * Creates array of Needs from tree map for needs that contain "text"
     * if "text" is null, array will contain all needs in treemap
     * @param text, if this string is passed in only add needs contaiting to Array
     * @return Array of Needs
     */
    private Need[] getNeedsArray(String userName, String text){
        swapCurrentUser(userName);
        ArrayList<Need> needsArrayList = new ArrayList<>();

        for (Need need : currentUserNeeds.values()){
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
        Need[] usersArray = getNeedsArray();
        //use the object mapper to serialize jave objects to JSON 
        //Issues will raise an IOException
        objectMapper.writeValue(new File(filename), needsArray);
        return true;

    }
    /**
     * Load from JSON into the map
     * Increment through every user, adding their name and their funding basket to the TreeMap
     * @return true if read successfully
     */
    private boolean load() throws IOException{
        Map<Integer,Need> needs = new TreeMap<>();
        // nextId = 0;

        users = new TreeMap<>();

        //use object mapper to deserialize  file into needsArray
        FundingBasketDataWrapper userData = objectMapper.readValue(new File(filename), FundingBasketDataWrapper.class);
        
        List<User> usersArray = userData.getUsers();

        //add users to users tree map
        for (User user : usersArray){
            needs.clear(); // clear the temporary needs before each user
            currentUser = user.getName();
            Need[] needsArray = user.getBasket(); // set needsArray to the current user's funding basket

            //add needs to needs tree map
            for (Need need : needsArray){
            needs.put(need.getId(), need);
            // if (need.getId() > nextId)
            //     nextId = need.getId();

            users.put(user.getName(), needs);
            }
        }
        currentUserNeeds = users.get(currentUser); // sets the current user needs to whatever the last user was in the usersArray(the currentUser)
        
        // ++nextId;
        return true;
    }
    /**
     * Returns array of all needs in JSON
     * @return getNeedsArray
     */

    @Override
    public Need[] getNeeds() {
        synchronized(currentUserNeeds) {
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
        synchronized(currentUserNeeds) {
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
        synchronized(currentUserNeeds) {
            if (currentUserNeeds.containsKey(id))
                return currentUserNeeds.get(id);
            else
                return null;
        }

    }

    /**
     * Creates a new Need object and a new User if one was not present
     * 
     * @param userName: the name of the user the new need belongs to
     * @param need: the input object containing the id, name, value, quantity, and type
     * @return the new need
     * @throws I/O exception if an error occured while saving the new Need to storage
     */
    @Override
    public Need createdNeed(String userName, Need need) throws IOException {
        synchronized(need) {
            if (userName.equals(null)){ // if an username is not given, then return null
                return null;
            }
            if (need.getId() == 0){ // if an id is not given, then return null
                return null;
            }
            swapCurrentUser(userName);
            boolean exists = currentUserNeeds.values().stream().anyMatch(n -> n.getName().equalsIgnoreCase(need.getName()) || n.getId() == (need.getId()));
            if (exists){
                return null;
            }
            Need newNeed = new Need(need.getId(),need.getName(), need.getValue(), need.getQuantity(), need.getType());
            // add the newly created need to the (currentUserNeeds) Map and the current user's needs in the (users) Map
            currentUserNeeds.put(need.getId(),newNeed);
            users.put(currentUser, currentUserNeeds);
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
        synchronized(currentUserNeeds) {
            if (currentUserNeeds.containsKey(need.getId()) == false)
                return null;

            currentUserNeeds.put(need.getId(),need);
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
         synchronized(currentUserNeeds) {
            if (currentUserNeeds.containsKey(id)) {
                currentUserNeeds.remove(id);
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
        synchronized(currentUserNeeds) {
            if(currentUserNeeds.entrySet().removeIf(entry -> entry.getValue().getType().equals(type))){ // remove entry if it matches the given type
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
        synchronized(currentUserNeeds) {
            currentUserNeeds.clear();
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
        synchronized(currentUserNeeds) {
            if(currentUserNeeds.entrySet().removeIf(entry -> entry.getValue().getName().contains(name))){
                return save();
            }
            else 
                return false;
     }
    }

}
