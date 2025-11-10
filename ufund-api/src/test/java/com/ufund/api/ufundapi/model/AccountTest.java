package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit test for the Account class
 * 
 * Author: CTRL Team
 */
@Tag("Model-tier")
public class AccountTest {

    @Test
    public void testToString() {
        // Setup
        String username = "alice";
        String password = "pw";
        String role = "helper";
        Account account = new Account(username, password, role);

        String expected = String.format(Account.STRINGIFY, username, password, role);

        // Invoke
        String actual = account.toString();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testGettersAndSetters() {
        // Setup
        Account account = new Account("helper1234", "1234", "helper");

        // Invoke
        account.setUsername("helper5678");
        account.setPassword("5678");
        account.setRole("admin");

        // Analyze
        assertEquals("helper5678", account.getUsername());
        assertEquals("5678", account.getPassword());
        assertEquals("admin", account.getRole());
    }
}
