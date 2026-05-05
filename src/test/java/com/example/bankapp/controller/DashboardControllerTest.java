package com.example.bankapp.controller;

import com.example.bankapp.entity.Account;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {
    
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
    private DashboardController dashboardController;
    
    private Account testAccount;
    
    @BeforeEach
    void setUp() {
        testAccount = new Account("testuser", "password123");
        testAccount.setId(1L);
        testAccount.setBalance(1000.0);
        
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    void testDashboardWithAuthenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        String result = dashboardController.dashboard(model);
        
        assertEquals("dashboard", result);
        verify(model, times(1)).addAttribute("account", testAccount);
    }
    
    @Test
    void testDashboardWithoutAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        
        String result = dashboardController.dashboard(model);
        
        assertEquals("redirect:/login", result);
    }
    
    @Test
    void testDashboardUserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("nonexistent");
        when(accountService.findByUsername("nonexistent")).thenReturn(Optional.empty());
        
        String result = dashboardController.dashboard(model);
        
        assertEquals("redirect:/login", result);
    }
    
    @Test
    void testDepositSuccess() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        
        String result = dashboardController.deposit(100.0, model);
        
        assertEquals("redirect:/dashboard", result);
        verify(transactionService, times(1)).deposit(1L, 100.0);
        verify(accountService, times(1)).getAccountById(1L);
    }
    
    @Test
    void testDepositWithoutAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        
        String result = dashboardController.deposit(100.0, model);
        
        assertEquals("redirect:/login", result);
        verify(transactionService, times(0)).deposit(anyLong(), anyDouble());
    }
    
    @Test
    void testDepositNegativeAmount() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        String result = dashboardController.deposit(-50.0, model);
        
        assertEquals("dashboard", result);
        verify(model, times(1)).addAttribute("error", "Amount must be greater than 0");
        verify(transactionService, times(0)).deposit(anyLong(), anyDouble());
    }
    
    @Test
    void testDepositZeroAmount() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        String result = dashboardController.deposit(0.0, model);
        
        assertEquals("dashboard", result);
        verify(model, times(1)).addAttribute("error", "Amount must be greater than 0");
    }
    
    @Test
    void testWithdrawSuccess() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        
        String result = dashboardController.withdraw(100.0, model);
        
        assertEquals("redirect:/dashboard", result);
        verify(transactionService, times(1)).withdraw(1L, 100.0);
    }
    
    @Test
    void testWithdrawInsufficientBalance() {
        testAccount.setBalance(50.0);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        String result = dashboardController.withdraw(100.0, model);
        
        assertEquals("dashboard", result);
        verify(model, times(1)).addAttribute("error", "Insufficient balance");
        verify(transactionService, times(0)).withdraw(anyLong(), anyDouble());
    }
    
    @Test
    void testWithdrawNegativeAmount() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        String result = dashboardController.withdraw(-50.0, model);
        
        assertEquals("dashboard", result);
        verify(model, times(1)).addAttribute("error", "Amount must be greater than 0");
    }
    
    @Test
    void testTransferSuccess() {
        Account toAccount = new Account("recipient", "password123");
        toAccount.setId(2L);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(accountService.findByUsername("recipient")).thenReturn(Optional.of(toAccount));
        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        
        String result = dashboardController.transfer("recipient", 200.0, model);
        
        assertEquals("redirect:/dashboard", result);
        verify(transactionService, times(1)).transfer(1L, 2L, 200.0);
    }
    
    @Test
    void testTransferRecipientNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(accountService.findByUsername("nonexistent")).thenReturn(Optional.empty());
        
        String result = dashboardController.transfer("nonexistent", 200.0, model);
        
        assertEquals("dashboard", result);
        verify(model, times(1)).addAttribute("error", "Recipient account not found");
        verify(transactionService, times(0)).transfer(anyLong(), anyLong(), anyDouble());
    }
    
    @Test
    void testTransferInsufficientBalance() {
        testAccount.setBalance(50.0);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        String result = dashboardController.transfer("recipient", 200.0, model);
        
        assertEquals("dashboard", result);
        verify(model, times(1)).addAttribute("error", "Insufficient balance");
        verify(transactionService, times(0)).transfer(anyLong(), anyLong(), anyDouble());
    }
    
    @Test
    void testTransferNegativeAmount() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        String result = dashboardController.transfer("recipient", -100.0, model);
        
        assertEquals("dashboard", result);
        verify(model, times(1)).addAttribute("error", "Amount must be greater than 0");
    }
    
    @Test
    void testTransferZeroAmount() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(accountService.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        String result = dashboardController.transfer("recipient", 0.0, model);
        
        assertEquals("dashboard", result);
        verify(model, times(1)).addAttribute("error", "Amount must be greater than 0");
    }
}

