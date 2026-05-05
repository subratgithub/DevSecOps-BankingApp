package com.example.bankapp.service;

import com.example.bankapp.entity.Account;
import com.example.bankapp.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    
    private Account testAccount;
    
    @BeforeEach
    void setUp() {
        testAccount = new Account("testuser", "encoded_password");
        testAccount.setId(1L);
        testAccount.setBalance(1000.0);
    }
    
    @Test
    void testLoadUserByUsernameSuccess() {
        when(accountRepository.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");
        
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        verify(accountRepository, times(1)).findByUsername("testuser");
    }
    
    @Test
    void testLoadUserByUsernameNotFound() {
        when(accountRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        
        assertThrows(UsernameNotFoundException.class, 
                () -> customUserDetailsService.loadUserByUsername("nonexistent"));
        
        verify(accountRepository, times(1)).findByUsername("nonexistent");
    }
    
    @Test
    void testLoadUserByUsernameWithNullUsername() {
        when(accountRepository.findByUsername(null)).thenReturn(Optional.empty());
        
        assertThrows(UsernameNotFoundException.class, 
                () -> customUserDetailsService.loadUserByUsername(null));
        
        verify(accountRepository, times(1)).findByUsername(null);
    }
    
    @Test
    void testLoadUserByUsernameWithEmptyUsername() {
        when(accountRepository.findByUsername("")).thenReturn(Optional.empty());
        
        assertThrows(UsernameNotFoundException.class, 
                () -> customUserDetailsService.loadUserByUsername(""));
        
        verify(accountRepository, times(1)).findByUsername("");
    }
    
    @Test
    void testLoadUserByUsernameErrorMessage() {
        String username = "testuser";
        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());
        
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, 
                () -> customUserDetailsService.loadUserByUsername(username));
        
        assertTrue(exception.getMessage().contains(username));
        assertTrue(exception.getMessage().contains("User not found"));
    }
    
    @Test
    void testLoadUserByUsernameWithSpecialCharacters() {
        String specialUsername = "test_user@123";
        Account specialAccount = new Account(specialUsername, "password");
        when(accountRepository.findByUsername(specialUsername)).thenReturn(Optional.of(specialAccount));
        
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(specialUsername);
        
        assertNotNull(userDetails);
        assertEquals(specialUsername, userDetails.getUsername());
        verify(accountRepository, times(1)).findByUsername(specialUsername);
    }
    
    @Test
    void testLoadUserRoleAssignment() {
        when(accountRepository.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");
        
        assertNotNull(userDetails.getAuthorities());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }
}

