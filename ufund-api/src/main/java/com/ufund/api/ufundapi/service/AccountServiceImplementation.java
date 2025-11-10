package com.ufund.api.ufundapi.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.persistence.AccountDAO;
import com.ufund.api.ufundapi.persistence.FundingBasketDAO;

/**
 * Implementation of the AccountService interface
 * 
 * Author: CTRL Team
 */
@Service
public class AccountServiceImplementation implements AccountService {
    private final AccountDAO accountDAO;
    private final FundingBasketDAO fundingBasketDAO;

    /**
     * Constructs an AccountServiceImplementation instance
     * 
     * @param accountDAO the data access object for Account operations
     */
    @Autowired
    public AccountServiceImplementation(AccountDAO accountDAO, FundingBasketDAO fundingBasketDAO) {
        this.accountDAO = accountDAO;
        this.fundingBasketDAO = fundingBasketDAO;
    }

    /**
     * Returns array of all accounts
     * @return Account[] array of accounts
     * @throws IOException if an error occurs
     */
    @Override
    public Account[] getAccounts() throws IOException {
        Account[] list = accountDAO.getAccounts();
        return list;
    }

    /**
     * Returns array of all usernames
     * @return String[] array of usernames
     * @throws IOException if an error occurs
     */
    @Override
    public String[] getUsernames() throws IOException {
        String[] list = accountDAO.getUsernames();
        return list;
    }

    /**
     * Checks if an account exists for the given username
     * @param username the username to check
     * @return true if account exists, false otherwise
     * @throws IOException if an error occurs
     */
    @Override
    public boolean exists(String username) throws IOException {
        boolean present = accountDAO.exists(username);
        return present;
    }

    /**
     * Creates a new Account
     * @param account the Account to create
     * @return the created Account or null if duplicate
     * @throws IOException if an error occurs
     */
    @Override
    public Account create(Account account) throws IOException {
        Account created = accountDAO.create(account);
        if (created != null) {
            fundingBasketDAO.createUser(created.getUsername());
        }
        return created;
    }

    /**
     * Validates credentials
     * @param username the username
     * @param password the password
     * @return true if valid, false otherwise
     * @throws IOException if an error occurs
     */
    @Override
    public boolean validate(String username, String password) throws IOException {
        boolean ok = accountDAO.validate(username, password);
        return ok;
    }

    /**
     * Deletes an account
     * @param username the username to delete
     * @return true if deleted, false otherwise
     * @throws IOException if an error occurs
     */
    @Override
    public boolean delete(String username) throws IOException {
        if (username == null) {
            return false;
        }
        
        try {
            fundingBasketDAO.deleteUser(username);
        } catch (IOException e) {
        }

        boolean removed = accountDAO.delete(username);
        return removed;
    }
}
