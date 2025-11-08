package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.service.AccountService;

/**
 * Handles the REST API requests for the Account resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * Author: CTRL Team
 */
@RestController
@RequestMapping("accounts")
public class AccountController {
    private static final Logger LOG = Logger.getLogger(AccountController.class.getName());
    private final AccountService accountService;

    /**
     * Constructor
     * @param accountService injected AccountService
     */
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    /**
     * Handles HTTP GET requests to "/accounts"
     * @return a ResponseEntity containing HttpStatus.OK with the array of accounts,
     * HttpStatus.NO_CONTENT if the returned data is null,
     * or HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occurred.
     */
    @GetMapping("")
    public ResponseEntity<Account[]> getAccounts(){
        LOG.info("GET /accounts");
        try {
            Account[] accounts = accountService.getAccounts();
            if (accounts != null) {
                return new ResponseEntity<Account[]>(accounts, HttpStatus.OK);
            } else {
                return new ResponseEntity<Account[]>(HttpStatus.NO_CONTENT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<Account[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP GET requests to "/accounts/usernames"
     * @return a ResponseEntity containing HttpStatus.OK with the array of usernames,
     * HttpStatus.NO_CONTENT if the returned data is null,
     * or HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occurred.
     */
    @GetMapping("/usernames")
    public ResponseEntity<String[]> getUsernames(){
        LOG.info("GET /accounts/usernames");
        try {
            String[] usernames = accountService.getUsernames();
            if (usernames != null) {
                return new ResponseEntity<String[]>(usernames, HttpStatus.OK);
            } else {
                return new ResponseEntity<String[]>(HttpStatus.NO_CONTENT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<String[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP GET requests to "/accounts/exists/{username}"
     * @param username the username to check
     * @return a ResponseEntity containing HttpStatus.OK with true/false,
     * or HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occurred.
     */
    @GetMapping("/exists/{username}")
    public ResponseEntity<Boolean> exists(@PathVariable String username){
        LOG.info("GET /accounts/exists/" + username);
        try {
            boolean present = accountService.exists(username);
            return new ResponseEntity<Boolean>(present, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP POST requests to "/accounts"
     * @param account the Account to create
     * @return a ResponseEntity containing HttpStatus.CREATED if creation was successful,
     * HttpStatus.CONFLICT if the username already exists,
     * or HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occurred.
     */
    @PostMapping("")
    public ResponseEntity<Account> create(@RequestBody Account account){
        LOG.info("POST /accounts " + account);
        try {
            Account created = accountService.create(account);
            if (created != null) {
                return new ResponseEntity<Account>(created, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<Account>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP POST requests to "/accounts/login"
     * @param req the Account object containing username and password
     * @return a ResponseEntity containing HttpStatus.OK if credentials are valid,
     * HttpStatus.UNAUTHORIZED if invalid,
     * or HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occurred.
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Account req){
        LOG.info("POST /accounts/login");
        try {
            boolean ok = accountService.validate(req.getUsername(), req.getPassword());
            if (ok) {
                return new ResponseEntity<Void>(HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP DELETE requests to "/accounts/{username}"
     * @param username the username to delete
     * @return a ResponseEntity containing HttpStatus.OK if deletion was successful,
     * HttpStatus.NOT_FOUND if the username does not exist,
     * or HttpStatus.INTERNAL_SERVER_ERROR if an I/O error occurred.
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username){
        LOG.info("DELETE /accounts/" + username);
        try {
            boolean removed = accountService.delete(username);
            if (removed) {
                return new ResponseEntity<Void>(HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
