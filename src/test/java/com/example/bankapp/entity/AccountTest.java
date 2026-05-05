package com.example.bankapp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    
    private Account account;
    
    @BeforeEach
    void setUp() {
        account = new Account();
    }
    
    @Test
    void testDefaultConstructor() {
        assertNotNull(account);
        assertEquals(0.0, account.getBalance());
        assertNull(account.getId());
        assertNull(account.getUsername());
        assertNull(account.getPassword());
    }
    
    @Test
    void testConstructorWithUsernameAndPassword() {
        Account account = new Account("testuser", "password123");
        assertEquals("testuser", account.getUsername());
        assertEquals("password123", account.getPassword());
        assertEquals(0.0, account.getBalance());
    }
    
    @Test
    void testSetAndGetId() {
        account.setId(1L);
        assertEquals(1L, account.getId());
    }
    
    @Test
    void testSetAndGetUsername() {
        account.setUsername("john_doe");
        assertEquals("john_doe", account.getUsername());
    }
    
    @Test
    void testSetAndGetPassword() {
        account.setPassword("encrypted_password");
        assertEquals("encrypted_password", account.getPassword());
    }
    
    @Test
    void testSetAndGetBalance() {
        account.setBalance(1000.50);
        assertEquals(1000.50, account.getBalance());
    }
    
    @Test
    void testNegativeBalance() {
        account.setBalance(-500.0);
        assertEquals(-500.0, account.getBalance());
    }
    
    @Test
    void testZeroBalance() {
        account.setBalance(0.0);
        assertEquals(0.0, account.getBalance());
    }
    
    @Test
    void testLargeBalance() {
        account.setBalance(999999999.99);
        assertEquals(999999999.99, account.getBalance());
    }
    
    @Test
    void testAccountEquality() {
        Account account1 = new Account("user1", "pass1");
        account1.setId(1L);
        Account account2 = new Account("user1", "pass1");
        account2.setId(1L);
        
        assertEquals(account1.getId(), account2.getId());
        assertEquals(account1.getUsername(), account2.getUsername());
    }
}

