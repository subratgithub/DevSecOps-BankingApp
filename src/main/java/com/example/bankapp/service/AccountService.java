package com.example.bankapp.service;

import com.example.bankapp.entity.Account;
import com.example.bankapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Account registerAccount(String username, String password) {
        if (accountRepository.findByUsername(username).isPresent()) {
            return null; // User already exists
        }
        
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setBalance(0.0);
        
        return accountRepository.save(account);
    }
    
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
    
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }
    
    public void updateAccount(Account account) {
        accountRepository.save(account);
    }
    
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

