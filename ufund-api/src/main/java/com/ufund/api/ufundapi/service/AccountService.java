package com.ufund.api.ufundapi.service;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Account;

/**
 * Service interface for Account operations
 * 
 * Author: CTRL Team
 */
public interface AccountService {

    /**
     * Returns array of all accounts
     * @return an array of Accounts
     * @throws IOException if an error occurs
     */
    Account[] getAccounts() throws IOException;

    /**
     * Returns array of all usernames
     * @return an array of usernames
     * @throws IOException if an error occurs
     */
    String[] getUsernames() throws IOException;

    /**
     * Checks if an account exists for the given username
     * @param username the username to check
     * @return true if the account exists, false otherwise
     * @throws IOException if an error occurs
     */
    boolean exists(String username) throws IOException;

    /**
     * Creates a new Account
     * 
     * @param account the Account object to create
     * @return the newly created Account, or null if the username already exists
     * @throws IOException if an error occurs
     */
    Account create(Account account) throws IOException;

    /**
     * Validates an account's credentials
     * 
     * @param username the username to validate
     * @param password the password to validate
     * @return true if credentials are valid, false otherwise
     * @throws IOException if an error occurs
     */
    boolean validate(String username, String password) throws IOException;

    /**
     * Deletes an account by username
     * 
     * @param username the username to delete
     * @return true if the deletion was successful, false otherwise
     * @throws IOException if an error occurs
     */
    boolean delete(String username) throws IOException;
}
