package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.model.AccountsDataWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Test the AccountFileDAO class
 * 
 * Author: CTRL Team
 */
@Tag("Persistence-tier")
public class AccountFileDAOTest {
    AccountFileDAO accountFileDAO;
    Account[] testAccounts;
    ObjectMapper mockObjectMapper;

    private static final String FILENAME = "doesnt_matter_accounts.txt";

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file.
     * 
     * Note: AccountFileDAO's constructor checks file existence/length before
     * calling readValue(), so we create a small seed file to ensure load() uses
     * our mocked readValue(...) path (same pattern as FundingBasketFileDAOTest,
     * but adapted for AccountFileDAO's existence check).
     */
    @BeforeEach
    public void setupAccountFileDAO() throws IOException {
        // Create a non-empty seed file so load() calls readValue(...)
        File seed = new File(FILENAME);

        mockObjectMapper = mock(ObjectMapper.class);

        testAccounts = new Account[3];
        testAccounts[0] = new Account("helper123", "123", "helper");
        testAccounts[1] = new Account("helper456", "456", "helper");
        testAccounts[2] = new Account("admin", "admin", "admin");

        AccountsDataWrapper wrapper = new AccountsDataWrapper(Arrays.asList(testAccounts));

        when(mockObjectMapper.readValue(any(File.class), eq(AccountsDataWrapper.class)))
            .thenReturn(wrapper);
        doNothing().when(mockObjectMapper)
            .writeValue(any(File.class), any(AccountsDataWrapper.class));

        accountFileDAO = new AccountFileDAO(FILENAME, mockObjectMapper);
    }

    // getAccounts() Tests
    @Test
    public void testGetAccounts() {
        // Invoke
        Account[] accounts = accountFileDAO.getAccounts();

        // Analyze
        assertNotNull(accounts);
        assertEquals(testAccounts.length, accounts.length);

        // Order is not guaranteed; check membership
        assertTrue(Arrays.stream(accounts).anyMatch(a -> "helper123".equals(a.getUsername())));
        assertTrue(Arrays.stream(accounts).anyMatch(a -> "helper456".equals(a.getUsername())));
        assertTrue(Arrays.stream(accounts).anyMatch(a -> "admin".equals(a.getUsername())));
    }

    // getUsernames() Tests
    @Test
    public void testGetUsernames() {
        // Invoke
        String[] names = accountFileDAO.getUsernames();

        // Analyze
        assertNotNull(names);
        assertEquals(testAccounts.length, names.length);
        assertTrue(Arrays.asList(names).contains("helper123"));
        assertTrue(Arrays.asList(names).contains("helper456"));
        assertTrue(Arrays.asList(names).contains("admin"));
    }

    // exists() Tests
    @Test
    public void testExists() {
        assertTrue(accountFileDAO.exists("helper123"));
        assertTrue(accountFileDAO.exists("helper456"));
        assertTrue(accountFileDAO.exists("admin"));
        assertFalse(accountFileDAO.exists("ghost"));
    }

    @Test
    public void testExistsNull() {
        assertFalse(accountFileDAO.exists(null));
    }

    // validate() Tests
    @Test
    public void testValidate() {
        assertTrue(accountFileDAO.validate("helper123", "123"));
        assertTrue(accountFileDAO.validate("helper456", "456"));
        assertTrue(accountFileDAO.validate("admin", "admin"));

        assertFalse(accountFileDAO.validate("helper123", "wrong"));
        assertFalse(accountFileDAO.validate("ghost", "pw"));
    }

    // create() Tests
    @Test
    public void testCreateAccount() {
        // Setup
        Account newAcc = new Account("test1", "1", "helper");

        // Invoke (create throws IOException -> wrap in assertDoesNotThrow)
        Account created = assertDoesNotThrow(() -> accountFileDAO.create(newAcc), "Unexpected exception thrown");

        // Analyze
        assertNotNull(created);
        assertEquals("test1", created.getUsername());
        assertTrue(accountFileDAO.exists("test1"));
        assertEquals(testAccounts.length + 1, accountFileDAO.getAccounts().length);
    }

    @Test
    public void testCreateDuplicateReturnsNull() {
        // Duplicate username "admin"
        Account dup = new Account("admin", "something", "admin");

        Account result = assertDoesNotThrow(() -> accountFileDAO.create(dup), "Unexpected exception thrown");
        assertNull(result);

        // No change in count
        assertEquals(testAccounts.length, accountFileDAO.getAccounts().length);
    }

    @Test
    public void testCreateNullInputsReturnNull() {
        Account nullAccount = null;
        Account nullName = new Account(null, "pw", "helper");

        assertNull(assertDoesNotThrow(() -> accountFileDAO.create(nullAccount)));
        assertNull(assertDoesNotThrow(() -> accountFileDAO.create(nullName)));

        // Still only the seeded accounts
        assertEquals(testAccounts.length, accountFileDAO.getAccounts().length);
    }

    // delete() Tests
    @Test
    public void testDeleteAccount() {
        // Invoke
        boolean removed = assertDoesNotThrow(() -> accountFileDAO.delete("helper456"), "Unexpected exception thrown");

        // Analyze
        assertTrue(removed);
        assertFalse(accountFileDAO.exists("helper456"));
        assertEquals(testAccounts.length - 1, accountFileDAO.getAccounts().length);
    }

    @Test
    public void testDeleteAccountNotFound() {
        boolean removed = assertDoesNotThrow(() -> accountFileDAO.delete("ghost"), "Unexpected exception thrown");

        assertFalse(removed);
        assertEquals(testAccounts.length, accountFileDAO.getAccounts().length);
    }

    @Test
    public void testDeleteAccountNull() {
        boolean removed = assertDoesNotThrow(() -> accountFileDAO.delete(null), "Unexpected exception thrown");

        assertFalse(removed);
        assertEquals(testAccounts.length, accountFileDAO.getAccounts().length);
    }
}
