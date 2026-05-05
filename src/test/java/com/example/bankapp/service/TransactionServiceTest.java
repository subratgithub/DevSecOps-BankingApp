package com.example.bankapp.service;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @Mock
    private AccountService accountService;
    
    @InjectMocks
    private TransactionService transactionService;
    
    private Account testAccount;
    private Account recipientAccount;
    private Transaction testTransaction;
    
    @BeforeEach
    void setUp() {
        testAccount = new Account("testuser", "password123");
        testAccount.setId(1L);
        testAccount.setBalance(1000.0);
        
        recipientAccount = new Account("recipient", "password123");
        recipientAccount.setId(2L);
        recipientAccount.setBalance(500.0);
        
        testTransaction = new Transaction(1L, 100.0, "DEPOSIT", 1100.0);
        testTransaction.setId(1L);
    }
    
    // DEPOSIT TESTS
    @Test
    void testDepositSuccess() {
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        
        Transaction result = transactionService.deposit(1L, 100.0);
        
        assertNotNull(result);
        assertEquals("DEPOSIT", result.getType());
        assertEquals(100.0, result.getAmount());
        verify(accountService, times(1)).getAccountById(1L);
        verify(accountService, times(1)).updateAccount(any(Account.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    @Test
    void testDepositAccountNotFound() {
        when(accountService.getAccountById(999L)).thenReturn(null);
        
        Transaction result = transactionService.deposit(999L, 100.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testDepositNegativeAmount() {
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        
        Transaction result = transactionService.deposit(1L, -100.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testDepositZeroAmount() {
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        
        Transaction result = transactionService.deposit(1L, 0.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testDepositLargeAmount() {
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        
        Transaction result = transactionService.deposit(1L, 5000.0);
        
        assertNotNull(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    // WITHDRAW TESTS
    @Test
    void testWithdrawSuccess() {
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        Transaction withdrawTransaction = new Transaction(1L, 100.0, "WITHDRAW", 900.0);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(withdrawTransaction);
        
        Transaction result = transactionService.withdraw(1L, 100.0);
        
        assertNotNull(result);
        assertEquals("WITHDRAW", result.getType());
        assertEquals(100.0, result.getAmount());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    @Test
    void testWithdrawAccountNotFound() {
        when(accountService.getAccountById(999L)).thenReturn(null);
        
        Transaction result = transactionService.withdraw(999L, 100.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testWithdrawNegativeAmount() {
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        
        Transaction result = transactionService.withdraw(1L, -50.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testWithdrawZeroAmount() {
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        
        Transaction result = transactionService.withdraw(1L, 0.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testWithdrawInsufficientBalance() {
        testAccount.setBalance(50.0);
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        
        Transaction result = transactionService.withdraw(1L, 100.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testWithdrawExactBalance() {
        testAccount.setBalance(100.0);
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        Transaction withdrawTransaction = new Transaction(1L, 100.0, "WITHDRAW", 0.0);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(withdrawTransaction);
        
        Transaction result = transactionService.withdraw(1L, 100.0);
        
        assertNotNull(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    // TRANSFER TESTS
    @Test
    void testTransferSuccess() {
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        when(accountService.getAccountById(2L)).thenReturn(recipientAccount);
        Transaction transferTransaction = new Transaction(1L, 200.0, "TRANSFER", 800.0);
        transferTransaction.setToAccountId(2L);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transferTransaction);
        
        Transaction result = transactionService.transfer(1L, 2L, 200.0);
        
        assertNotNull(result);
        assertEquals("TRANSFER", result.getType());
        assertEquals(200.0, result.getAmount());
        assertEquals(2L, result.getToAccountId());
        verify(accountService, times(2)).updateAccount(any(Account.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    @Test
    void testTransferFromAccountNotFound() {
        when(accountService.getAccountById(999L)).thenReturn(null);
        
        Transaction result = transactionService.transfer(999L, 2L, 100.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testTransferToAccountNotFound() {
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        when(accountService.getAccountById(999L)).thenReturn(null);
        
        Transaction result = transactionService.transfer(1L, 999L, 100.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testTransferNegativeAmount() {
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        when(accountService.getAccountById(2L)).thenReturn(recipientAccount);
        
        Transaction result = transactionService.transfer(1L, 2L, -100.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testTransferZeroAmount() {
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        when(accountService.getAccountById(2L)).thenReturn(recipientAccount);
        
        Transaction result = transactionService.transfer(1L, 2L, 0.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testTransferInsufficientBalance() {
        testAccount.setBalance(50.0);
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        when(accountService.getAccountById(2L)).thenReturn(recipientAccount);
        
        Transaction result = transactionService.transfer(1L, 2L, 100.0);
        
        assertNull(result);
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
    
    @Test
    void testTransferExactBalance() {
        testAccount.setBalance(200.0);
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        when(accountService.getAccountById(2L)).thenReturn(recipientAccount);
        Transaction transferTransaction = new Transaction(1L, 200.0, "TRANSFER", 0.0);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transferTransaction);
        
        Transaction result = transactionService.transfer(1L, 2L, 200.0);
        
        assertNotNull(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    // TRANSACTION HISTORY TESTS
    @Test
    void testGetTransactionHistorySuccess() {
        Transaction txn1 = new Transaction(1L, 100.0, "DEPOSIT", 100.0);
        Transaction txn2 = new Transaction(1L, 50.0, "WITHDRAW", 50.0);
        List<Transaction> transactions = Arrays.asList(txn1, txn2);
        
        when(transactionRepository.findByFromAccountIdOrderByTimestampDesc(1L)).thenReturn(transactions);
        
        List<Transaction> result = transactionService.getTransactionHistory(1L);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findByFromAccountIdOrderByTimestampDesc(1L);
    }
    
    @Test
    void testGetTransactionHistoryEmpty() {
        when(transactionRepository.findByFromAccountIdOrderByTimestampDesc(1L)).thenReturn(Arrays.asList());
        
        List<Transaction> result = transactionService.getTransactionHistory(1L);
        
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(transactionRepository, times(1)).findByFromAccountIdOrderByTimestampDesc(1L);
    }
}

