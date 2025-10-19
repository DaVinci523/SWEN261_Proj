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

import com.ufund.api.ufundapi.persistence.FundingBasketDAO;
// Final Imports will be our FundingBasketDAO interface & our Need model
import com.ufund.api.ufundapi.persistence.FundingBasketDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

/**
 * Handles the REST API requests for the Need resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author SWEN Faculty,, Modified by CTRL Team
 */
@RestController
@RequestMapping("funding_basket_needs")
public class FundingBasketController {
    private static final Logger LOG = Logger.getLogger(FundingBasketController.class.getName());
    private FundingBasketDAO fundingBasketDAO;

    /**
     * Constructor
     * @param fundingBasketController
     */
    public FundingBasketController(FundingBasketDAO fundingBasketDAO){
        this.fundingBasketDAO = fundingBasketDAO;
    }

    /**
     * Handles HTTP GET requests to the "/users"
     * @return a Response entity containing HttpStatus.OK if the array of users is returned,
     * a HttpStatus.NO_CONTENT if the returned data is null
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("/users")
    public ResponseEntity<User[]> getUsers(){
        LOG.info("GET /funding_basket_needs/users");
        try {
            User user[] = fundingBasketDAO.getUsers();
            if (user != null)
                return new ResponseEntity<>(user,HttpStatus.OK); 
            else
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

    /**
     * Handles HTTP GET requests to the "/needs/id"
     * @param userName: the user to search in
     * @param id: the searched objects id
     * @return a Response entity containing HttpStatus.OK if the search was successful,
     * a HttpStatus.NOT_FOUND if the search fails due to a non existent id
     *  HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("{userName}/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable String userName, @PathVariable int id){
         LOG.info("GET /funding_basket_needs/" + userName + "/" + id);
        try {
            Need need = fundingBasketDAO.getNeed(userName, id);
            if (need != null){
                return new ResponseEntity<Need>(need,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP GET requests to the "userName/needs"
     * @param userName: the user to retrieve from
     * @return a Response entity containing HttpStatus.OK if the array of needs is returned,
     * a HttpStatus.NO_CONTENT if the returned data is null
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("{userName}/needs")
    public ResponseEntity<Need[]> getNeeds(@PathVariable String userName){
        LOG.info("GET /funding_basket_needs/" + userName + "/needs");
        try {
            Need need[] = fundingBasketDAO.getNeeds(userName);
            if (need != null){
                return new ResponseEntity<>(need,HttpStatus.OK); 
            }
            else{
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

    /**
     * Handles HTTP GET requests to the "/needs/?name= "name""
     * @param userName: the user to search from
     * @param name , check if need contains String name
     * @return a Response entity containing HttpStatus.OK if the search was successful,
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @GetMapping("/{userName}/") 
    public ResponseEntity<Need[]> searchNeeds(@PathVariable String userName, @RequestParam String name){
        LOG.info("GET /funding_basket_needs/"+ userName +"/?name=" + name);
        try {
            Need[] needs = fundingBasketDAO.searchNeeds(userName, name);
            return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP POST requests to the "/needs"
     * @param userName: the user to create in
     * @param need: the object to be created
     * @return a Response entity containing HttpStatus.CREATED if the creation was successful,
     * a HttpStatus.CONFLICT if the creation fails due to a conflict, or a HttpStatus.INTERNAL_SERVER_ERROR
     * if an I/O error occured. 
     */
    @PostMapping("/{userName}") 
    public ResponseEntity<Need> createNeed(@PathVariable String userName, @RequestBody Need need){
        LOG.info("POST /funding_basket_needs/" + userName + " " + need);

        try {
            Need newNeed = fundingBasketDAO.createdNeed(userName, need);
            System.out.println(newNeed);

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
     * @param userName: the user to update
     * @param need: the object to be updated
     * @return a Response entity containing HttpStatus.OK if the update was successful,
     * a HttpStatus.NOT_FOUND if need doesn't exist
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @PutMapping("/{userName}")
    public ResponseEntity<Need> updateNeed(@PathVariable String userName, @RequestBody Need need){
        LOG.info("PUT /funding_basket_needs/" + userName + " " + need);
        
        try {
            Need updated = fundingBasketDAO.updateNeed(userName, need);
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
     * @param userName: the user to delete from
     * @param id , id of need to be deleted
     * @return a Response entity containing HttpStatus.OK if the deletionn was successful,
     * a HttpStatus.NOT_FOUND if a need of id doesn't exist
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @DeleteMapping("/{userName}/{id}") 
    public ResponseEntity<Need> deleteNeedId(@PathVariable String userName, @PathVariable int id){
       LOG.info("DELETE /funding_basket_needs/" + userName + "/" + id);
        try{

            if(fundingBasketDAO.deleteNeed(userName, id)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }


        } catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP DELETE requests to the "/needs"
     * @param userName: the user to delete from
     * @param type , type of need to be deleted
     * @return a Response entity containing HttpStatus.OK if the deletionn was successful,
     * a HttpStatus.NOT_FOUND if a need of id doesn't exist
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @DeleteMapping("/{userName}/type/{type}")
    public ResponseEntity<Need> deleteNeedType(@PathVariable String userName, @PathVariable String type){
       LOG.info("DELETE /funding_basket_needs/" + userName + "/type/" + type);
        try{
            if(fundingBasketDAO.deleteNeedType(userName, type)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }


        } catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Handles HTTP DELETE requests to the "/needs"
     * @param userName: the user to delete from
     * @return a Response entity containing HttpStatus.OK if the deletionn was successful,
     * a HttpStatus.NOT_FOUND if a need of id doesn't exist
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @DeleteMapping("/{userName}")
    public ResponseEntity<Need> deleteAll(@PathVariable String userName){
       LOG.info("DELETE /funding_basket_needs/" + userName);
        try{
            if(fundingBasketDAO.deleteAll(userName))
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
     * @param userName: the user to delete from
     * @param name , string to check if name contains
     * @return a Response entity containing HttpStatus.OK if the deletionn was successful,
     * a HttpStatus.NOT_FOUND if a need of id doesn't exist
     * HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occured. 
     */
    @DeleteMapping("/{userName}/name/{name}")
    public ResponseEntity<Need> deleteNeedName(@PathVariable String userName, @PathVariable String name){
       LOG.info("DELETE /funding_basket_needs" + userName + "/name/" + name);
        try{
            if(fundingBasketDAO.deleteNeedName(userName, name))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        } catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
