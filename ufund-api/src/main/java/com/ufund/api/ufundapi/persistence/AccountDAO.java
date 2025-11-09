package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Account;

/**
 * Interfacing for Account objects
 * 
 * Provides methods to access and modify account data stored in a JSON file.
 * Methods may throw IOException if the underlying storage cannot be read or written.
 * 
 * @author CTRL Team
 */
public interface AccountDAO {

    /**
     * Returns array of all accounts
     * @return Account[] array of accounts (could be empty)
     * @throws IOException if an I/O error occurs
     */
    Account[] getAccounts() throws IOException;

    /**
     * Returns array of all usernames
     * @return String[] array of usernames (could be empty)
     * @throws IOException if an I/O error occurs
     */
    String[] getUsernames() throws IOException;

    /**
     * Checks if an account exists for the given username
     * @param username the username to check
     * @return true if the account exists, false otherwise
     * @throws IOException if an I/O error occurs
     */
    boolean exists(String username) throws IOException;

    /**
     * Creates a new Account object
     * 
     * @param account the input object containing the username, password, and role
     * @return the created Account if successful, null if username already exists (conflict)
     * @throws IOException if an error occurred while saving the new Account to storage
     */
    Account create(Account account) throws IOException;

    /**
     * Validates credentials for an account
     * @param username the username to validate
     * @param password the password to validate
     * @return true if credentials are valid, false otherwise
     * @throws IOException if an I/O error occurs
     */
    boolean validate(String username, String password) throws IOException;

    /**
     * Deletes an account with the specified username
     * @param username the username to delete
     * @return true if file was updated successfully, false if deletion failed or username not found
     * @throws IOException if an I/O error occurs
     */
    boolean delete(String username) throws IOException;
}
