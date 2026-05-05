package com.example.bankapp.controller;

import com.example.bankapp.entity.Account;
import com.example.bankapp.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    
    @Mock
    private AccountService accountService;
    
    @Mock
    private Model model;
    
    @InjectMocks
    private AuthController authController;
    
    private MockMvc mockMvc;
    private Account testAccount;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        testAccount = new Account("testuser", "password123");
        testAccount.setId(1L);
    }
    
    @Test
    void testIndexRedirectsToLogin() {
        String result = authController.index();
        
        assertEquals("redirect:/login", result);
    }
    
    @Test
    void testLoginPageReturnsLoginView() {
        String result = authController.loginPage();
        
        assertEquals("login", result);
    }
    
    @Test
    void testRegisterPageReturnsRegisterView() {
        String result = authController.registerPage();
        
        assertEquals("register", result);
    }
    
    @Test
    void testRegisterSuccessfully() {
        when(accountService.registerAccount("newuser", "password123")).thenReturn(testAccount);
        
        String result = authController.register("newuser", "password123", model);
        
        assertEquals("redirect:/login?success=true", result);
        verify(accountService, times(1)).registerAccount("newuser", "password123");
        verify(model, times(1)).addAttribute("success", "Registration successful! Please login.");
    }
    
    @Test
    void testRegisterUserAlreadyExists() {
        when(accountService.registerAccount("testuser", "password123")).thenReturn(null);
        
        String result = authController.register("testuser", "password123", model);
        
        assertEquals("register", result);
        verify(accountService, times(1)).registerAccount("testuser", "password123");
        verify(model, times(1)).addAttribute("error", "User already exists");
    }
    
    @Test
    void testRegisterWithEmptyUsername() {
        when(accountService.registerAccount("", "password123")).thenReturn(null);
        
        String result = authController.register("", "password123", model);
        
        assertEquals("register", result);
        verify(model, times(1)).addAttribute("error", "User already exists");
    }
    
    @Test
    void testRegisterWithEmptyPassword() {
        when(accountService.registerAccount("newuser", "")).thenReturn(testAccount);
        
        String result = authController.register("newuser", "", model);
        
        assertEquals("redirect:/login?success=true", result);
        verify(accountService, times(1)).registerAccount("newuser", "");
    }
    
    @Test
    void testRegisterWithNullUsername() {
        when(accountService.registerAccount(null, "password123")).thenReturn(null);
        
        String result = authController.register(null, "password123", model);
        
        assertEquals("register", result);
        verify(model, times(1)).addAttribute("error", "User already exists");
    }
    
    @Test
    void testRegisterWithNullPassword() {
        when(accountService.registerAccount("newuser", null)).thenReturn(testAccount);
        
        String result = authController.register("newuser", null, model);
        
        assertEquals("redirect:/login?success=true", result);
        verify(accountService, times(1)).registerAccount("newuser", null);
    }
    
    @Test
    void testRegisterWithSpecialCharactersInUsername() {
        String specialUsername = "user@test_123";
        when(accountService.registerAccount(specialUsername, "password123")).thenReturn(testAccount);
        
        String result = authController.register(specialUsername, "password123", model);
        
        assertEquals("redirect:/login?success=true", result);
        verify(accountService, times(1)).registerAccount(specialUsername, "password123");
    }
    
    @Test
    void testRegisterWithLongPassword() {
        String longPassword = "a".repeat(100);
        when(accountService.registerAccount("newuser", longPassword)).thenReturn(testAccount);
        
        String result = authController.register("newuser", longPassword, model);
        
        assertEquals("redirect:/login?success=true", result);
        verify(accountService, times(1)).registerAccount("newuser", longPassword);
    }
    
    @Test
    void testMultipleRegistrationAttempts() {
        when(accountService.registerAccount("user1", "pass1")).thenReturn(testAccount);
        when(accountService.registerAccount("user2", "pass2")).thenReturn(testAccount);
        
        String result1 = authController.register("user1", "pass1", model);
        String result2 = authController.register("user2", "pass2", model);
        
        assertEquals("redirect:/login?success=true", result1);
        assertEquals("redirect:/login?success=true", result2);
        verify(accountService, times(2)).registerAccount(anyString(), anyString());
    }
}

