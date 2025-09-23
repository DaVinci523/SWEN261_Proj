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
import java.net.http.HttpResponse.ResponseInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

// Final Imports will be our NeedsCupboardDAO interface & our Need model
import com.ufund.api.ufundapi.persistence.NeedsCupboardDAO;
import com.ufund.api.ufundapi.model.Need;

@RestController
@RequestMapping("needs")
public class NeedsCupboardController {
    private static final Logger LOG = Logger.getLogger(
        NeedsCupboardController.class.getName());
    private NeedsCupboardDAO needsCupboardDAO;

    /**
     * Constructor
     * @param needsCupboardController
     */
    public NeedsCupboardController(NeedsCupboardController needsCupboardController){
    }


    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id){
        return null;
    }


    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds(){
        return null;
    }

    @GetMapping("/")
    public ResponseEntity<Need[]> searchNeeds(@RequestParam String name){
        return null;
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
