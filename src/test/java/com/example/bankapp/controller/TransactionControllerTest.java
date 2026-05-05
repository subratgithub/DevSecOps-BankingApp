package com.example.bankapp.controller;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.service.AccountService;
import com.example.bankapp.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    
    @Mock
    private AccountService accountService;
    
    @Mock
    private TransactionService transactionService;
    
    @Mock
    private Model model;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private SecurityContext securityContext;
    
    @InjectMocks
    private TransactionController transactionController;
    
    private Account testAccount;
    private List<Transaction> testTransactions;
    
    @BeforeEach
    void setUp() {
        testAccount = new Account("testuser", "password123");
        testAccount.setId(1L);
        testAccount.setBalance(1000.0);
        
        Transaction txn1 = new Transaction(1L, 100.0, "DEPOSIT", 100.0);
        txn1.setId(1L);
        txn1.setTimestamp(LocalDateTime.now());
        
        Transaction txn2 = new Transaction(1L, 50.0, "WITHDRAW", 50.0);
        txn2.setId(2L);
        txn2.setTimestamp(LocalDateTime.now().minusHours(1));
        
        testTransactions = Arrays.asList(txn1, txn2);
        
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    void testTransactionHistoryWithAuthenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(transactionService.getTransactionHistory(1L)).thenReturn(testTransactions);
        
        String result = transactionController.transactionHistory(model);
        
        assertEquals("transactions", result);
        verify(model, times(1)).addAttribute("transactions", testTransactions);
        verify(model, times(1)).addAttribute("account", testAccount);
        verify(transactionService, times(1)).getTransactionHistory(1L);
    }
    
    @Test
    void testTransactionHistoryWithoutAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        
        String result = transactionController.transactionHistory(model);
        
        assertEquals("redirect:/login", result);
        verify(transactionService, times(0)).getTransactionHistory(anyLong());
    }
    
    @Test
    void testTransactionHistoryUserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("nonexistent");
        when(accountService.findByUsername("nonexistent")).thenReturn(Optional.empty());
        
        String result = transactionController.transactionHistory(model);
        
        assertEquals("redirect:/login", result);
        verify(transactionService, times(0)).getTransactionHistory(anyLong());
    }
    
    @Test
    void testTransactionHistoryEmpty() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(transactionService.getTransactionHistory(1L)).thenReturn(Collections.emptyList());
        
        String result = transactionController.transactionHistory(model);
        
        assertEquals("transactions", result);
        verify(model, times(1)).addAttribute("transactions", Collections.emptyList());
        verify(model, times(1)).addAttribute("account", testAccount);
    }
    
    @Test
    void testTransactionHistoryMultipleTransactions() {
        Transaction txn3 = new Transaction(1L, 200.0, "TRANSFER", 200.0);
        txn3.setId(3L);
        txn3.setToAccountId(2L);
        txn3.setTimestamp(LocalDateTime.now().minusDays(1));
        
        List<Transaction> manyTransactions = Arrays.asList(testTransactions.get(0), testTransactions.get(1), txn3);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(transactionService.getTransactionHistory(1L)).thenReturn(manyTransactions);
        
        String result = transactionController.transactionHistory(model);
        
        assertEquals("transactions", result);
        verify(model, times(1)).addAttribute("transactions", manyTransactions);
    }
    
    @Test
    void testTransactionHistoryNullAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);
        
        String result = transactionController.transactionHistory(model);
        
        assertEquals("redirect:/login", result);
        verify(transactionService, times(0)).getTransactionHistory(anyLong());
    }
    
    @Test
    void testTransactionHistoryTransactionTypes() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(transactionService.getTransactionHistory(1L)).thenReturn(testTransactions);
        
        String result = transactionController.transactionHistory(model);
        
        assertEquals("transactions", result);
        verify(model, times(1)).addAttribute("transactions", testTransactions);
        verify(model, times(1)).addAttribute("account", testAccount);
    }
    
    @Test
    void testTransactionHistoryAccountBalance() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(transactionService.getTransactionHistory(1L)).thenReturn(testTransactions);
        
        String result = transactionController.transactionHistory(model);
        
        assertEquals("transactions", result);
        verify(model, times(1)).addAttribute("account", testAccount);
        verify(accountService, times(1)).findByUsername("testuser");
    }
}

