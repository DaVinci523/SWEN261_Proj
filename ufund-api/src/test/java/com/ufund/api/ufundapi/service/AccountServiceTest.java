package com.ufund.api.ufundapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.persistence.AccountDAO;
import com.ufund.api.ufundapi.persistence.FundingBasketDAO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

/**
 * Unit tests for the AccountServiceImplementation
 * 
 * @author CTRL Team
 */
public class AccountServiceTest {
    private AccountDAO mockAccountDAO;
    private FundingBasketDAO mockFundingBasketDAO;
    private AccountServiceImplementation service;

    // Similar to FundingBasket tests: initialize a fixed array for reuse
    private Account[] testAccounts;
    private static final String USERNAME = "helper123";

    /**
     * Initialize the mock DAOs and service before each test
     */
    @BeforeEach
    public void setup() {
        mockAccountDAO = mock(AccountDAO.class);
        mockFundingBasketDAO = mock(FundingBasketDAO.class);
        service = new AccountServiceImplementation(mockAccountDAO, mockFundingBasketDAO);

        testAccounts = new Account[] {
            new Account("helper123", "123", "helper"),
            new Account("helper456", "456", "helper"),
            new Account("admin", "admin", "admin")
        };
    }

    /**
     * Tests that getAccounts() returns all the accounts correctly
     */
    @Test
    public void testGetAccounts() throws IOException {
        when(mockAccountDAO.getAccounts()).thenReturn(testAccounts);

        Account[] actual = service.getAccounts();

        assertArrayEquals(testAccounts, actual);
        verify(mockAccountDAO, times(1)).getAccounts();
    }

    /**
     * Tests that getUsernames() returns all usernames correctly
     */
    @Test
    public void testGetUsernames() throws IOException {
        String[] expected = { "helper123", "helper456", "admin" };
        when(mockAccountDAO.getUsernames()).thenReturn(expected);

        String[] actual = service.getUsernames();

        assertArrayEquals(expected, actual);
        verify(mockAccountDAO, times(1)).getUsernames();
    }

    /**
     * Tests that exists(username) delegates to the DAO
     */
    @Test
    public void testExists() throws IOException {
        when(mockAccountDAO.exists(USERNAME)).thenReturn(true);

        boolean present = service.exists(USERNAME);

        assertTrue(present);
        verify(mockAccountDAO, times(1)).exists(USERNAME);
    }

    /**
     * Tests that create(account) returns the created account
     * and creates a FundingBasket user
     */
    @Test
    public void testCreate() throws IOException {
        Account newAccount = new Account("test1", "1", "helper");
        when(mockAccountDAO.create(newAccount)).thenReturn(newAccount);

        Account created = service.create(newAccount);

        assertEquals(newAccount, created);
        verify(mockAccountDAO, times(1)).create(newAccount);
        verify(mockFundingBasketDAO, times(1)).createUser("test1");
    }

    /**
     * Tests that create(account) returns null on duplicate
     * and does not create a FundingBasket user
     */
    @Test
    public void testCreateDuplicate() throws IOException {
        Account dup = new Account("helper123", "x", "helper");
        when(mockAccountDAO.create(dup)).thenReturn(null);

        Account result = service.create(dup);

        assertNull(result);
        verify(mockAccountDAO, times(1)).create(dup);
        verify(mockFundingBasketDAO, never()).createUser(anyString());
    }

    /**
     * Tests that validate(username, password) delegates to DAO
     */
    @Test
    public void testValidate() throws IOException {
        when(mockAccountDAO.validate("admin", "admin")).thenReturn(true);

        boolean ok = service.validate("admin", "admin");

        assertTrue(ok);
        verify(mockAccountDAO, times(1)).validate("admin", "admin");
    }

    /**
     * Tests that delete(username) removes FundingBasket user then account
     */
    @Test
    public void testDelete() throws IOException {
        when(mockAccountDAO.delete("helper456")).thenReturn(true);

        boolean result = service.delete("helper456");

        assertTrue(result);
        verify(mockFundingBasketDAO, times(1)).deleteUser("helper456");
        verify(mockAccountDAO, times(1)).delete("helper456");
    }

    /**
     * Tests that delete(username) returns false when account not found
     */
    @Test
    public void testDeleteNotFound() throws IOException {
        when(mockAccountDAO.delete("ghost")).thenReturn(false);

        boolean result = service.delete("ghost");

        assertFalse(result);
        verify(mockFundingBasketDAO, times(1)).deleteUser("ghost");
        verify(mockAccountDAO, times(1)).delete("ghost");
    }
}
