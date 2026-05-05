package com.example.bankapp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    
    private Transaction transaction;
    
    @BeforeEach
    void setUp() {
        transaction = new Transaction();
    }
    
    @Test
    void testDefaultConstructor() {
        assertNotNull(transaction);
        assertNotNull(transaction.getTimestamp());
        assertNull(transaction.getId());
        assertNull(transaction.getFromAccountId());
    }
    
    @Test
    void testConstructorWithParameters() {
        Double amount = 100.0;
        Double balanceAfter = 500.0;
        Transaction txn = new Transaction(1L, amount, "DEPOSIT", balanceAfter);
        
        assertEquals(1L, txn.getFromAccountId());
        assertEquals(amount, txn.getAmount());
        assertEquals("DEPOSIT", txn.getType());
        assertEquals(balanceAfter, txn.getBalanceAfter());
        assertNotNull(txn.getTimestamp());
    }
    
    @Test
    void testSetAndGetId() {
        transaction.setId(100L);
        assertEquals(100L, transaction.getId());
    }
    
    @Test
    void testSetAndGetFromAccountId() {
        transaction.setFromAccountId(1L);
        assertEquals(1L, transaction.getFromAccountId());
    }
    
    @Test
    void testSetAndGetToAccountId() {
        transaction.setToAccountId(2L);
        assertEquals(2L, transaction.getToAccountId());
    }
    
    @Test
    void testSetAndGetAmount() {
        transaction.setAmount(250.50);
        assertEquals(250.50, transaction.getAmount());
    }
    
    @Test
    void testSetAndGetType() {
        transaction.setType("WITHDRAW");
        assertEquals("WITHDRAW", transaction.getType());
    }
    
    @Test
    void testSetAndGetTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        transaction.setTimestamp(now);
        assertEquals(now, transaction.getTimestamp());
    }
    
    @Test
    void testSetAndGetBalanceAfter() {
        transaction.setBalanceAfter(1000.0);
        assertEquals(1000.0, transaction.getBalanceAfter());
    }
    
    @Test
    void testDepositTransaction() {
        Transaction deposit = new Transaction(1L, 500.0, "DEPOSIT", 500.0);
        assertEquals("DEPOSIT", deposit.getType());
        assertEquals(500.0, deposit.getAmount());
        assertEquals(500.0, deposit.getBalanceAfter());
    }
    
    @Test
    void testWithdrawTransaction() {
        Transaction withdraw = new Transaction(1L, 100.0, "WITHDRAW", 400.0);
        assertEquals("WITHDRAW", withdraw.getType());
        assertEquals(100.0, withdraw.getAmount());
        assertEquals(400.0, withdraw.getBalanceAfter());
    }
    
    @Test
    void testTransferTransaction() {
        Transaction transfer = new Transaction(1L, 200.0, "TRANSFER", 300.0);
        transfer.setToAccountId(2L);
        assertEquals("TRANSFER", transfer.getType());
        assertEquals(200.0, transfer.getAmount());
        assertEquals(2L, transfer.getToAccountId());
    }
    
    @Test
    void testNullToAccountId() {
        transaction.setToAccountId(null);
        assertNull(transaction.getToAccountId());
    }
}

