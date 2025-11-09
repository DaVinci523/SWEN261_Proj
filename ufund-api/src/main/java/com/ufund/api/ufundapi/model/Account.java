package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an Account (authentication record)
 *  @author CTRL
 */
public class Account {
    static final String STRINGIFY = "Account [username=%s, password=%s, role=%s]";

    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
    @JsonProperty("role")     private String role;

    /**
     * Create an Account with given username, password, and role
     * @param username Username for this account
     * @param password Password for this account
     * @param role     Role for this account (e.g., HELPER, ADMIN)
     */
    public Account(@JsonProperty("username") String username,
                   @JsonProperty("password") String password,
                   @JsonProperty("role") String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters //
    /** @return the username */
    public String getUsername() { return username; }

    /** @return the password */
    public String getPassword() { return password; }

    /** @return the role */
    public String getRole() { return role; }

    // Setters (for deserialization or updates) //
    /** @param username new username */
    public void setUsername(String username) { this.username = username; }

    /** @param password new password */
    public void setPassword(String password) { this.password = password; }

    /** @param role new role */
    public void setRole(String role) { this.role = role; }

    /**
     * @return String representation of an Account
     */
    @Override
    public String toString() {
        return String.format(STRINGIFY, username, password, role);
    }
}
