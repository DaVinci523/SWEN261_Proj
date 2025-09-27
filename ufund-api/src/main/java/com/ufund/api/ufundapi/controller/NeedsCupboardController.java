package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import java.net.http.HttpResponse.ResponseInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

// Final Imports will be our NeedsCupboardDAO interface & our Need model
import com.ufund.api.ufundapi.persistence.NeedsCupboardDAO;
import com.ufund.api.ufundapi.model.Need;

@RestController
@RequestMapping("needs")
public class NeedsCupboardController {
    private static final Logger LOG = Logger.getLogger(NeedsCupboardController.class.getName());
    private NeedsCupboardDAO needsCupboardDAO;

    /**
     * Constructor
     * @param needsCupboardController
     */
    public NeedsCupboardController(NeedsCupboardDAO needsCupboardDAO){
        this.needsCupboardDAO = needsCupboardDAO;
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
            Need need = needsCupboardDAO.getNeed(id);
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
     * a HttpStatus.NOT_FOUND if the return fails
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds(){
        LOG.info("GET /needs");
        try {
            Need need[] = needsCupboardDAO.getNeeds();
            if (need != null)
                return new ResponseEntity<>(need,HttpStatus.OK); 
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

    /**
     * Handles HTTP GET requests to the "/needs/?name= "name""
     * @param name , check if need contains String name
     * @return a Response entity containing HttpStatus.OK if the search was successful,
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("/")
    public ResponseEntity<Need[]> searchNeeds(@RequestParam String name){
        LOG.info("GET /needs/?name=" + name);
        try {
            Need[] needs = needsCupboardDAO.searchNeeds(name);
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
            Need newNeed = needsCupboardDAO.createdNeed(need);

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
            Need updated = needsCupboardDAO.updateNeed(need);
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
    public ResponseEntity<Need> deleteNeed(@PathVariable int id){
       LOG.info("DELETE /needs/" + id);
        try{

            if(needsCupboardDAO.deleteNeed(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        } catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
