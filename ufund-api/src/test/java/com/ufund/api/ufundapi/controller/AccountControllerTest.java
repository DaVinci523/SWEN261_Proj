package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Account Controller class
 * 
 * Author: CTRL Team
 */
@Tag("Controller-tier")
public class AccountControllerTest {
    private AccountController accountController;
    private AccountService mockAccountService;

    private static final String USERNAME = "user";

    /**
     * Before each test, create a new AccountController and inject
     * a mocked AccountService
     */
    @BeforeEach
    public void setupAccountController() {
        mockAccountService = mock(AccountService.class);
        accountController = new AccountController(mockAccountService);
    }

    /**
     * Tests the getAccounts method
     */
    @Test
    public void testGetAccounts() throws IOException {
        // Setup
        Account[] accounts = { new Account("helper1234", "1234", "helper") };
        when(mockAccountService.getAccounts()).thenReturn(accounts);

        // Invoke
        ResponseEntity<Account[]> response = accountController.getAccounts();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
    }

    @Test
    public void testGetAccountsFalse() throws IOException {
        // Setup
        when(mockAccountService.getAccounts()).thenReturn(null);

        // Invoke
        ResponseEntity<Account[]> response = accountController.getAccounts();

        // Analyze
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetAccountsException() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockAccountService).getAccounts();

        // Invoke
        ResponseEntity<Account[]> response = accountController.getAccounts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Tests the getUsernames method
     */
    @Test
    public void testGetUsernames() throws IOException {
        // Setup
        String[] names = { "helper1234", "helper5678" };
        when(mockAccountService.getUsernames()).thenReturn(names);

        // Invoke
        ResponseEntity<String[]> response = accountController.getUsernames();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("helper1234", response.getBody()[0]);
        assertEquals("helper5678", response.getBody()[1]);
    }

    @Test
    public void testGetUsernamesHandleException() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockAccountService).getUsernames();

        // Invoke
        ResponseEntity<String[]> response = accountController.getUsernames();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Tests the exists method
     */
    @Test
    public void testExists() throws IOException {
        // Setup
        when(mockAccountService.exists(eq(USERNAME))).thenReturn(true);

        // Invoke
        ResponseEntity<Boolean> response = accountController.exists(USERNAME);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testExistsHandleException() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockAccountService).exists(eq(USERNAME));

        // Invoke
        ResponseEntity<Boolean> response = accountController.exists(USERNAME);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Tests the create method
     */
    @Test
    public void testCreateAccount() throws IOException {
        // Setup
        Account req = new Account("helper333", "333", "helper");
        when(mockAccountService.create(eq(req))).thenReturn(req);

        // Invoke
        ResponseEntity<Account> response = accountController.create(req);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(req, response.getBody());
    }

    @Test
    public void testCreateAccountFailed() throws IOException {
        // Setup
        Account req = new Account("alice", "pw", "helper");
        when(mockAccountService.create(eq(req))).thenReturn(null);

        // Invoke
        ResponseEntity<Account> response = accountController.create(req);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateAccountHandleException() throws IOException {
        // Setup
        Account req = new Account("alice", "pw", "helper");
        when(mockAccountService.create(eq(req))).thenThrow(new IOException());

        // Invoke
        ResponseEntity<Account> response = accountController.create(req);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Tests the login method
     */
    @Test
    public void testLogin() throws IOException {
        // Setup
        Account req = new Account(USERNAME, "pw", "helper");
        when(mockAccountService.validate(eq(USERNAME), eq("pw"))).thenReturn(true);

        // Invoke
        ResponseEntity<Void> response = accountController.login(req);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLoginUnauthorized() throws IOException {
        // Setup
        Account req = new Account(USERNAME, "wrong", "helper");
        when(mockAccountService.validate(eq(USERNAME), eq("wrong"))).thenReturn(false);

        // Invoke
        ResponseEntity<Void> response = accountController.login(req);

        // Analyze
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testLoginHandleException() throws IOException {
        // Setup
        Account req = new Account(USERNAME, "pw", "helper");
        doThrow(new IOException()).when(mockAccountService).validate(eq(USERNAME), eq("pw"));

        // Invoke
        ResponseEntity<Void> response = accountController.login(req);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Tests the delete method
     * @throws IOException
     */
    @Test
    public void testDeleteAccount() throws IOException {
        // Setup
        when(mockAccountService.delete(eq(USERNAME))).thenReturn(true);

        // Invoke
        ResponseEntity<Void> response = accountController.delete(USERNAME);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteAccountNotFound() throws IOException {
        // Setup
        when(mockAccountService.delete(eq(USERNAME))).thenReturn(false);

        // Invoke
        ResponseEntity<Void> response = accountController.delete(USERNAME);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteAccountHandleException() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockAccountService).delete(eq(USERNAME));

        // Invoke
        ResponseEntity<Void> response = accountController.delete(USERNAME);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
