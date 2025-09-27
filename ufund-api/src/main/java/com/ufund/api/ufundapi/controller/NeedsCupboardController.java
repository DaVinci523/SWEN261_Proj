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


    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id){
         LOG.info("GET /heroes/" + id);
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


    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds(){
        LOG.info("GET /needs");
        try {
            Need need[] = needsCupboardDAO.getNeeds(); // gets all needs as an array of needs
            if (need != null)
                return new ResponseEntity<>(need,HttpStatus.OK); // returns the array of needs
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) { //INTERNAL_SERVER_ERROR if otherwise
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

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

    @PostMapping("") 
    public ResponseEntity<Need> createNeed(@RequestBody Need need){
        return null;
    }

    @PutMapping("")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need){
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteHero(@PathVariable int id){
        return null;
    }

}
