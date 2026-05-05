package com.example.bankapp.controller;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.service.AccountService;
import com.example.bankapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class TransactionController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TransactionService transactionService;
    
    private Account getLoggedInAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            Optional<Account> account = accountService.findByUsername(username);
            return account.orElse(null);
        }
        return null;
    }
    
    @GetMapping("/transactions")
    public String transactionHistory(Model model) {
        Account account = getLoggedInAccount();
        if (account == null) {
            return "redirect:/login";
        }
        
        List<Transaction> transactions = transactionService.getTransactionHistory(account.getId());
        model.addAttribute("transactions", transactions);
        model.addAttribute("account", account);
        
        return "transactions";
    }
}

