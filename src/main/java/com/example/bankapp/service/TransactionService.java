package com.example.bankapp.service;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountService accountService;
    
    public Transaction deposit(Long accountId, Double amount) {
        Account account = accountService.getAccountById(accountId);
        if (account != null && amount > 0) {
            account.setBalance(account.getBalance() + amount);
            accountService.updateAccount(account);
            
            Transaction transaction = new Transaction(accountId, amount, "DEPOSIT", account.getBalance());
            return transactionRepository.save(transaction);
        }
        return null;
    }
    
    public Transaction withdraw(Long accountId, Double amount) {
        Account account = accountService.getAccountById(accountId);
        if (account != null && amount > 0 && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountService.updateAccount(account);
            
            Transaction transaction = new Transaction(accountId, amount, "WITHDRAW", account.getBalance());
            return transactionRepository.save(transaction);
        }
        return null;
    }
    
    public Transaction transfer(Long fromAccountId, Long toAccountId, Double amount) {
        Account fromAccount = accountService.getAccountById(fromAccountId);
        Account toAccount = accountService.getAccountById(toAccountId);
        
        if (fromAccount != null && toAccount != null && amount > 0 && fromAccount.getBalance() >= amount) {
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);
            
            accountService.updateAccount(fromAccount);
            accountService.updateAccount(toAccount);
            
            Transaction transaction = new Transaction(fromAccountId, amount, "TRANSFER", fromAccount.getBalance());
            transaction.setToAccountId(toAccountId);
            return transactionRepository.save(transaction);
        }
        return null;
    }
    
    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findByFromAccountIdOrderByTimestampDesc(accountId);
    }
}

