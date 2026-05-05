package com.example.bankapp.controller;

import com.example.bankapp.entity.Account;
import com.example.bankapp.service.AccountService;
import com.example.bankapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class DashboardController {
    
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
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Account account = getLoggedInAccount();
        if (account == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("account", account);
        return "dashboard";
    }
    
    @PostMapping("/deposit")
    public String deposit(@RequestParam Double amount, Model model) {
        Account account = getLoggedInAccount();
        if (account == null) {
            return "redirect:/login";
        }
        
        if (amount <= 0) {
            model.addAttribute("error", "Amount must be greater than 0");
            model.addAttribute("account", account);
            return "dashboard";
        }
        
        transactionService.deposit(account.getId(), amount);
        account = accountService.getAccountById(account.getId()); // Refresh account
        model.addAttribute("account", account);
        
        return "redirect:/dashboard";
    }
    
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Double amount, Model model) {
        Account account = getLoggedInAccount();
        if (account == null) {
            return "redirect:/login";
        }
        
        if (amount <= 0) {
            model.addAttribute("error", "Amount must be greater than 0");
            model.addAttribute("account", account);
            return "dashboard";
        }
        
        if (amount > account.getBalance()) {
            model.addAttribute("error", "Insufficient balance");
            model.addAttribute("account", account);
            return "dashboard";
        }
        
        transactionService.withdraw(account.getId(), amount);
        account = accountService.getAccountById(account.getId()); // Refresh account
        model.addAttribute("account", account);
        
        return "redirect:/dashboard";
    }
    
    @PostMapping("/transfer")
    public String transfer(@RequestParam String toUsername, @RequestParam Double amount, Model model) {
        Account fromAccount = getLoggedInAccount();
        if (fromAccount == null) {
            return "redirect:/login";
        }
        
        if (amount <= 0) {
            model.addAttribute("error", "Amount must be greater than 0");
            model.addAttribute("account", fromAccount);
            return "dashboard";
        }
        
        if (amount > fromAccount.getBalance()) {
            model.addAttribute("error", "Insufficient balance");
            model.addAttribute("account", fromAccount);
            return "dashboard";
        }
        
        Optional<Account> toAccountOpt = accountService.findByUsername(toUsername);
        if (toAccountOpt.isEmpty()) {
            model.addAttribute("error", "Recipient account not found");
            model.addAttribute("account", fromAccount);
            return "dashboard";
        }
        
        transactionService.transfer(fromAccount.getId(), toAccountOpt.get().getId(), amount);
        fromAccount = accountService.getAccountById(fromAccount.getId()); // Refresh account
        model.addAttribute("account", fromAccount);
        
        return "redirect:/dashboard";
    }
}

