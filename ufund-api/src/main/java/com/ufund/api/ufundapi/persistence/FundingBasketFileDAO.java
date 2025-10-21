package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
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
            load();  // load the users from the file
    }

    /**
     * Swaps the current user to the specified userName, setting their respective data
     * @param userName the user that will be swapped to
     * @return
     */
    private boolean swapCurrentUser(String userName){
        synchronized(currentUser){
            currentUser = userName;

            currentUserNeeds = new TreeMap<>();

            try{
                for (Need need : users.get(currentUser)){
                    currentUserNeeds.put(need.getId(), need);
                }
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
    private User[] getUsersArray(){
        return getUsersArray(null);
    }

    /**
     * Creates array of all Users from tree map for users 
     * if "text" is null, array will contain all needs in treemap
     * @param text, if this string is passed in only add users contaiting to Array
     * @return Array of Users
     */
    private User[] getUsersArray(String text){
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
     * @param userName the user this method will get the needs of
     * @return Array of Needs, could be null
     */
    private Need[] getNeedsArray(String userName){
        return getNeedsArray(userName, null);
    }

    /**
     * Creates array of Needs from tree map for needs that contain "text"
     * if "text" is null, array will contain all needs in treemap
     * @param userName the user this method will get the needs of
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
        // Set needs in currentUserNeeds to Users
        ArrayList<Need> currentUserNeedsArrayList = new ArrayList<>();

        // Add the values of the currentUserNeeds to an array (currentNeedsArrayList)
        for (Need need : currentUserNeeds.values()){
            currentUserNeedsArrayList.add(need);
        }

        Need[] currentNeedsArray = new Need[currentUserNeedsArrayList.size()];

        currentUserNeedsArrayList.toArray(currentNeedsArray); // set currentNeedsArrayList to a Need[] (currentNeedsArray)

        users.put(currentUser, currentNeedsArray); // Update user needs

        // Save
        User[] usersArray = getUsersArray();

        // turns the array into a list and wraps it as a FundingBAsketDataWrapper, 
        // which is written to the file in the required format
        List<User> usersList = Arrays.asList(usersArray);
        FundingBasketDataWrapper wrappedList = new FundingBasketDataWrapper(usersList);

        objectMapper.writeValue(new File(filename), wrappedList); // Fixes angular checkout bug by writing to the file twice as to clear excess data.
        //use the object mapper to serialize jave objects to JSON 
        //Issues will raise an IOException
        objectMapper.writeValue(new File(filename), wrappedList);
        return true;

    }
    /**
     * Load from JSON into the map
     * Increment through every user, adding their name and their funding basket to the TreeMap
     * @return true if read successfully
     */
    private boolean load() throws IOException{
        users = new TreeMap<>();

        //use object mapper to deserialize  file into needsArray
        FundingBasketDataWrapper userData = objectMapper.readValue(new File(filename), FundingBasketDataWrapper.class);
        
        List<User> usersArray = userData.getUsers();

        //add users to users tree map
        for (User user : usersArray){

            currentUser = user.getName();

            Need[] needsArray = user.getBasket(); // set needsArray to the current user's funding basket
            
            users.put(user.getName(), needsArray);

        }

        swapCurrentUser(currentUser);// sets the current user needs to whatever the last user was in the usersArray(the currentUser)
        
        return true;
    }

    /**
     * Returns array of all users
     * @return getNeedsArray
     */

    @Override
    public User[] getUsers() {
        synchronized(currentUserNeeds) {
            return getUsersArray();
        }
    }

    /**
     * Returns array of all needs for a specified user
     * @param userName the user this method will modify
     * @return getNeedsArray
     */

    @Override
    public Need[] getNeeds(String userName) {
        swapCurrentUser(userName);
        synchronized(currentUserNeeds) {
            return getNeedsArray(currentUser);
        }
    }
    /**
     * Search for needs containing text passed in
     * @param userName the user this method will modify
     * @param String text
     * @return getNeedsArray that contains text
     */
    @Override
    public Need[] searchNeeds(String userName, String text) throws IOException {
        swapCurrentUser(userName);
        synchronized(currentUserNeeds) {
            return getNeedsArray(userName, text);
        }
    }

    /**
     * Returns single need with id
     * @param userName the user this method will modify
     * @param int id id to search for 
     * @return need if found, null if no id in needs JSON
     */
    @Override
    public Need getNeed(String userName, int id) throws IOException {
        swapCurrentUser(userName);
        synchronized(currentUserNeeds) {
            if (currentUserNeeds.containsKey(id))
                return currentUserNeeds.get(id);
            else
                return null;
        }

    }

    /**
     * Creates a new Need object and a new User if one was not present
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
            // add the newly created need to the (currentUserNeeds) Map
            currentUserNeeds.put(need.getId(), newNeed);

            //add the current user's needs in the (users) Map
            ArrayList<Need> needsArrayList = new ArrayList<Need>(currentUserNeeds.values());
            Need[] needsArray = new Need[needsArrayList.size()];
            needsArrayList.toArray(needsArray);
            users.put(currentUser, needsArray);

            save();
            return newNeed;
        }
    }

    /**
     * Update need's contents, ie)name, value, quantity, type
     * @param userName the user this method will modify
     * @param  Need need
     * @return Need updated need
     */
    @Override
    public Need updateNeed(String userName, Need need) throws IOException {
        swapCurrentUser(userName);
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
     * @param userName the user this method will modify
     * @param int id
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    @Override
    public boolean deleteNeed(String userName, int id) throws IOException {
        swapCurrentUser(userName);
        synchronized(currentUserNeeds) {
           if (currentUserNeeds.containsKey(id)) {
               currentUserNeeds.remove(id);
               boolean saved = save();
               return saved;
           }
           else
               return false;
        }
    }

    /**
     * Delete all needs of specific type
     * Seemed liike using a removeIf is the best way to do this 
     * since you cant iterate through a tree map and delete things without throwing error.
     * @param userName the user this method will modify
     * @param string type
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    @Override
    public boolean deleteNeedType(String userName, String type) throws IOException {
        swapCurrentUser(userName);
        synchronized(currentUserNeeds) {
            if(currentUserNeeds.entrySet().removeIf(entry -> entry.getValue().getType().equals(type))){ // remove entry if it matches the given type
               boolean saved = save();
               return saved;
            }
            else
                return false;
     }
    }

    /**
     * Delete all needs
     * @param userName the user this method will modify
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    @Override
    public boolean deleteAll(String userName) throws IOException {
        swapCurrentUser(userName);
        synchronized(currentUserNeeds) {
            currentUserNeeds.clear();
            boolean saved = save();
            return saved;
     }
    }

    /**
     * Delete all needs of specific name
     * @param userName the user this method will modify
     * @return true if file was updated succesfully
     * @return false, if deletion failed
     */
    @Override
    public boolean deleteNeedName(String userName, String name) throws IOException {
        swapCurrentUser(userName);
        synchronized(currentUserNeeds) {
            if(currentUserNeeds.entrySet().removeIf(entry -> entry.getValue().getName().contains(name))){
               boolean saved = save();
               return saved;
            }
            else 
                return false;
     }
    }

}
