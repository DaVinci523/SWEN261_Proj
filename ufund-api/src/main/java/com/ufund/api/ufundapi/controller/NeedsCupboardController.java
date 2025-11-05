package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

// Final Imports will be our NeedsCupboardDAO interface & our Need model
import com.ufund.api.ufundapi.service.NeedsCupboardService;
import com.ufund.api.ufundapi.model.Need;

/**
 * Handles the REST API requests for the Need resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author SWEN Faculty,, Modified by CTRL Team
 */
@RestController
@RequestMapping("needs")
public class NeedsCupboardController {
    private static final Logger LOG = Logger.getLogger(NeedsCupboardController.class.getName());
    private final NeedsCupboardService needsCupboardService;

    /**
     * Constructor
     * @param needsCupboardController
     */
    public NeedsCupboardController(NeedsCupboardService needsCupboardService){
        this.needsCupboardService = needsCupboardService;
    }

    /**
     * Handles HTTP GET requests to the "/needs/id"
     * @param id: the searched objects id
     * @return a Response entity containing HttpStatus.OK if the search was successful,
     * a HttpStatus.NOT_FOUND if the search fails due to a non existent id
     *  HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id){
         LOG.info("GET /needs/" + id);
        try {
            Need need = needsCupboardService.getNeedById(id);
            if (need != null)
                return new ResponseEntity<Need>(need,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP GET requests to the "/needs"
     * @return a Response entity containing HttpStatus.OK if the array of needs is returned,
     * a HttpStatus.NO_CONTENT if the returned data is null
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds(){
        LOG.info("GET /needs");
        try {
            Need need[] = needsCupboardService.getAllNeeds();
            if (need != null)
                return new ResponseEntity<>(need,HttpStatus.OK); 
            else
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

    /**
     * Handles HTTP GET requests to the "/needs/name/" + name"
     * @param name , check if need contains String name
     * @return a Response entity containing HttpStatus.OK if the search was successful,
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Need[]> searchNeeds(@PathVariable String name){
        LOG.info("GET /needs/name/" + name);
        try {
            Need[] needs = needsCupboardService.searchNeeds(name);
            return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP GET requests to the "/needs/type/" + type"
     * @param text , check if need type contains String text
     * @return a Response entity containing HttpStatus.OK if the search was successful,
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<Need[]> searchNeedsType(@PathVariable String type){
        LOG.info("GET /needs/type/" + type);
        try {
            Need[] needs = needsCupboardService.searchNeedsType(type);
            return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP GET requests to the "/needs/quantity/" + val"
     * @param val , integer of quantity to compare to
     * @return a Response entity containing HttpStatus.OK if the search was successful,
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("/quantity/{val}")
    public ResponseEntity<Need[]> searchNeedsQuantity(@PathVariable Integer val){
        LOG.info("GET /needs/quantity/" + val);
        try {
            Need[] needs = needsCupboardService.searchNeedsQuantity(val);
            return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP GET requests to the "/needs/value/" + val"
     * @param val , integer of value to compare to
     * @return a Response entity containing HttpStatus.OK if the search was successful,
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("/value/{val}")
    public ResponseEntity<Need[]> searchNeedsValue(@PathVariable Double val){
        LOG.info("GET /needs/value/" + val);
        try {
            Need[] needs = needsCupboardService.searchNeedsValue(val);
            return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP POST requests to the "/needs"
     * @param need: the object to be created
     * @return a Response entity containing HttpStatus.CREATED if the creation was successful,
     * a HttpStatus.CONFLICT if the creation fails due to a conflict, or a HttpStatus.INTERNAL_SERVER_ERROR
     * if an I/O error occured. 
     */
    @PostMapping("") 
    public ResponseEntity<Need> createNeed(@RequestBody Need need){
        LOG.info("POST /needs " + need);

        try {
            Need newNeed = needsCupboardService.createNeed(need);

            if (newNeed != null){
                return new ResponseEntity<>(newNeed, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP PUT requests to the "/needs"
     * @param need: the object to be updated
     * @return a Response entity containing HttpStatus.OK if the update was successful,
     * a HttpStatus.NOT_FOUND if need doesn't exist
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @PutMapping("")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need){
        LOG.info("PUT /needs " + need);
        
        try {
            Need updated = needsCupboardService.updateNeed(need);
            if (updated != null) {
                return new ResponseEntity<Need>(updated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP PUT requests to the "/needs"
     * @param userName: the user to update
     * @param id: the id of the need to be updated
     * @param visible: the visibility state to set the need
     * @return a Response entity containing HttpStatus.OK if the update was successful,
     * a HttpStatus.NOT_FOUND if need doesn't exist
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @PutMapping("/{id}/visible/")
    public ResponseEntity<Need> updateNeedVisibility(@PathVariable int id, @RequestParam boolean visible){
        LOG.info("PUT /needs/" + id + "/visible/?visible=" + visible );
        
        try {

            Need need = needsCupboardService.getNeedById(id);
            need.setVisible(visible); // set the retireved need's visibility

            Need updated = needsCupboardService.updateNeed(need);
            System.out.println(updated);
            if (updated != null) {
                return new ResponseEntity<Need>(updated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP DELETE requests to the "/needs"
     * @param id , id of need to be deleted
     * @return a Response entity containing HttpStatus.OK if the deletionn was successful,
     * a HttpStatus.NOT_FOUND if a need of id doesn't exist
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeedId(@PathVariable int id){
       LOG.info("DELETE /needs/" + id);
        try{
            if(needsCupboardService.deleteNeedById(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        } catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP DELETE requests to the "/needs"
     * @param type , type of need to be deleted
     * @return a Response entity containing HttpStatus.OK if the deletionn was successful,
     * a HttpStatus.NOT_FOUND if a need of id doesn't exist
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @DeleteMapping("/type/{type}")
    public ResponseEntity<Need> deleteNeedType(@PathVariable String type){
       LOG.info("DELETE /needs/type/" + type);
        try{
            if(needsCupboardService.deleteNeedByType(type))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        } catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Handles HTTP DELETE requests to the "/needs"
     * @return a Response entity containing HttpStatus.OK if the deletionn was successful,
     * a HttpStatus.NOT_FOUND if a need of id doesn't exist
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @DeleteMapping("")
    public ResponseEntity<Need> deleteAll(){
       LOG.info("DELETE /needs");
        try{
            if(needsCupboardService.deleteAllNeeds())
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        } catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } 

    /**
     * Handles HTTP DELETE requests to the "/needs"
     * @param name , string to check if name contains
     * @return a Response entity containing HttpStatus.OK if the deletionn was successful,
     * a HttpStatus.NOT_FOUND if a need of id doesn't exist
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Need> deleteNeedName(@PathVariable String name){
       LOG.info("DELETE /needs/name/" + name);
        try{
            if(needsCupboardService.deleteNeedByName(name))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        } catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
