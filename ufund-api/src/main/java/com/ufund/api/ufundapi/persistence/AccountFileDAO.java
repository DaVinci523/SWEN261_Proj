package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.model.AccountsDataWrapper;

/**
 * Implements the functionality for JSON file-based persistence for Accounts
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed.
 * 
 * The storage file is expected to have the following shape:
 * {
 *   "users": [
 *     { "username": "...", "password": "...", "role": "..." }
 *   ]
 * }
 * 
 * This DAO maintains an in-memory index keyed by username and writes the full
 * structure back to disk on each mutation.
 * 
 * Author: CTRL Team
 */
@Component
public class AccountFileDAO implements AccountDAO {
    private final ObjectMapper objectMapper;
    private final String filename;

    // In-memory index of accounts keyed by username
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    /**
     * Create Accounts DAO (Data Access Object)
     * @param filename filename to read/write from
     * @param objectMapper provides deserialization & serialization for JSON
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public AccountFileDAO(@Value("${auth.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Load from JSON into the in-memory map
     * Iterates through every account, adding them to the ConcurrentHashMap
     * @throws IOException if read fails
     */
    private void load() throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            save();
            return;
        }

        AccountsDataWrapper data = objectMapper.readValue(f, AccountsDataWrapper.class);
        List<Account> list = null;
        if (data != null) {
            list = data.getUsers();
        }

        if (list != null) {
            for (Account a : list) {
                if (a != null && a.getUsername() != null) {
                    accounts.put(a.getUsername(), a);
                }
            }
        }
    }

    /**
     * Saves accounts from map into file
     * @return true, if written successfully
     * @throws IOException if write fails
     */
    private boolean save() throws IOException {
        ArrayList<Account> list = new ArrayList<>(accounts.values());
        AccountsDataWrapper wrapped = new AccountsDataWrapper(list);
        objectMapper.writeValue(new File(filename), wrapped);
        return true;
    }

    /**
     * Returns array of all accounts
     * @return Account[] array of accounts (could be empty)
     */
    @Override
    public Account[] getAccounts() {
        Account[] array = accounts.values().toArray(new Account[0]);
        return array;
    }

    /**
     * Returns array of all usernames
     * @return String[] array of usernames (could be empty)
     */
    @Override
    public String[] getUsernames() {
        String[] array = accounts.keySet().toArray(new String[0]);
        return array;
    }

    /**
     * Checks if an account exists for the given username
     * @param username the username to check
     * @return true if the account exists, false otherwise
     */
    @Override
    public boolean exists(String username) {
        if (username == null) {
            return false;
        }

        boolean present = accounts.containsKey(username);
        if (present) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Creates a new Account object
     * 
     * If the username already exists, this method returns null to indicate a conflict.
     * 
     * @param account the input object containing the username, password, and role
     * @return the new Account if created; null if username already exists
     * @throws IOException if an error occurred while saving the new Account to storage
     */
    @Override
    public Account create(Account account) throws IOException {
        if (account == null) {
            return null;
        }

        if (account.getUsername() == null) {
            return null;
        }

        Account prev = accounts.putIfAbsent(
            account.getUsername(),
            new Account(account.getUsername(), account.getPassword(), account.getRole())
        );

        if (prev != null) {
            return null;
        }

        save();
        Account created = accounts.get(account.getUsername());
        return created;
    }

    /**
     * Validates credentials for an account
     * @param username the username to validate
     * @param password the password to validate
     * @return true if credentials are valid, false otherwise
     */
    @Override
    public boolean validate(String username, String password) {
        Account a = accounts.get(username);

        if (a == null) {
            return false;
        }

        boolean same = Objects.equals(a.getPassword(), password);
        if (same) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes an account with the specified username
     * @param username the username to delete
     * @return true if file was updated successfully, false if deletion failed or username not found
     * @throws IOException if an I/O error occurs while saving data
     */
    @Override
    public boolean delete(String username) throws IOException {
        if (username == null) {
            return false;
        }

        Account removed = accounts.remove(username);

        if (removed != null) {
            save();
            return true;
        } else {
            return false;
        }
    }
}
