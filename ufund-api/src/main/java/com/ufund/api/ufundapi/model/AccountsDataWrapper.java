package com.ufund.api.ufundapi.model;

import java.util.List;

/**
 * Used to wrap users from the FundingBasket into a list of users
 * @author CTRL
 */

public class AccountsDataWrapper {
    private List<Account> users;

    public AccountsDataWrapper() {}
    
    public AccountsDataWrapper(List<Account> users) {
        this.users = users;
    }
    
    public List<Account> getUsers() {
        return users;
    }
    
    public void setUsers(List<Account> users) {
        this.users = users;
    }
}
