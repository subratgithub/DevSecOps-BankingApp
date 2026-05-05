package com.example.bankapp.service;

import com.example.bankapp.entity.Account;
import com.example.bankapp.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private AccountService accountService;
    
    private Account testAccount;
    
    @BeforeEach
    void setUp() {
        testAccount = new Account("testuser", "password123");
        testAccount.setId(1L);
        testAccount.setBalance(1000.0);
    }
    
    @Test
    void testRegisterAccountSuccess() {
        when(accountRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        
        Account result = accountService.registerAccount("newuser", "password123");
        
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(accountRepository, times(1)).findByUsername("newuser");
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    
    @Test
    void testRegisterAccountUserAlreadyExists() {
        when(accountRepository.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        Account result = accountService.registerAccount("testuser", "password123");
        
        assertNull(result);
        verify(accountRepository, times(1)).findByUsername("testuser");
        verify(accountRepository, times(0)).save(any(Account.class));
    }
    
    @Test
    void testFindByUsernameExists() {
        when(accountRepository.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        Optional<Account> result = accountService.findByUsername("testuser");
        
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(accountRepository, times(1)).findByUsername("testuser");
    }
    
    @Test
    void testFindByUsernameNotExists() {
        when(accountRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        
        Optional<Account> result = accountService.findByUsername("nonexistent");
        
        assertTrue(result.isEmpty());
        verify(accountRepository, times(1)).findByUsername("nonexistent");
    }
    
    @Test
    void testGetAccountByIdSuccess() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        
        Account result = accountService.getAccountById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(accountRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetAccountByIdNotFound() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        
        Account result = accountService.getAccountById(999L);
        
        assertNull(result);
        verify(accountRepository, times(1)).findById(999L);
    }
    
    @Test
    void testUpdateAccount() {
        testAccount.setBalance(2000.0);
        when(accountRepository.save(testAccount)).thenReturn(testAccount);
        
        accountService.updateAccount(testAccount);
        
        verify(accountRepository, times(1)).save(testAccount);
    }
    
    @Test
    void testValidatePasswordMatch() {
        when(passwordEncoder.matches("password123", "encoded_password")).thenReturn(true);
        
        boolean result = accountService.validatePassword("password123", "encoded_password");
        
        assertTrue(result);
        verify(passwordEncoder, times(1)).matches("password123", "encoded_password");
    }
    
    @Test
    void testValidatePasswordMismatch() {
        when(passwordEncoder.matches("wrongpassword", "encoded_password")).thenReturn(false);
        
        boolean result = accountService.validatePassword("wrongpassword", "encoded_password");
        
        assertFalse(result);
        verify(passwordEncoder, times(1)).matches("wrongpassword", "encoded_password");
    }
    
    @Test
    void testRegisterAccountWithNullUsername() {
        when(accountRepository.findByUsername(null)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        
        Account result = accountService.registerAccount(null, "password123");
        
        assertNotNull(result);
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    
    @Test
    void testRegisterAccountWithEmptyPassword() {
        when(accountRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("")).thenReturn("encoded_empty");
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        
        Account result = accountService.registerAccount("newuser", "");
        
        assertNotNull(result);
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}

