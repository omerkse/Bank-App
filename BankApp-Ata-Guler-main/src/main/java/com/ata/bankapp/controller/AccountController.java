package com.ata.bankapp.controller;

import com.ata.bankapp.client.AccountClient;
import com.ata.bankapp.model.Account;
import com.ata.bankapp.model.Transaction;
import com.ata.bankapp.service.AccountService;
import com.ata.bankapp.service.TransactionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final AccountClient accountClient;

    @PostMapping
    public ResponseEntity<Map<String, String>> createAccount(@RequestBody Account account) {
        logger.info("Creating account: {}", account);
        accountService.createAccount(account);
        logger.info("Account created successfully.");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account created successfully.");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Map<String, String>> updateAccount(@PathVariable Long accountId, @RequestBody Account updatedAccount) {
        logger.info("Updating account ID: {} with data: {}", accountId, updatedAccount);
        accountService.updateAccount(accountId, updatedAccount);
        logger.info("Account ID: {} updated successfully.", accountId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account updated successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        logger.info("Fetching all accounts");
        List<Account> accounts = accountClient.getAllAccounts();
        logger.info("Fetched {} accounts", accounts.size());
        return accounts;
    }

    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable Long accountId) {
        logger.info("Fetching account with ID: {}", accountId);
        Account account = accountClient.getAccount(accountId);
        logger.info("Fetched account with ID: {}", accountId);
        return account;
    }

    @GetMapping("/user/{userId}")
    public List<Account> getAccountsByUserId(@PathVariable Long userId) {
        logger.info("Fetching accounts for user ID: {}", userId);
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        logger.info("Fetched {} accounts for user ID: {}", accounts.size(), userId);
        return accounts;
    }

    @GetMapping("/user/{userId}/first")
    public Account getFirstAccountByUserId(@PathVariable Long userId) {
        logger.info("Fetching first account for user ID: {}", userId);
        Account account = accountService.getFirstAccountByUserId(userId);
        logger.info("Fetched first account for user ID: {}", userId);
        return account;
    }

    @GetMapping("/{accountId}/balance")
    public Double getAccountBalance(@PathVariable Long accountId) {
        logger.info("Fetching balance for account ID: {}", accountId);
        Double balance = accountService.getAccountBalance(accountId);
        logger.info("Fetched balance for account ID: {} is {}", accountId, balance);
        return balance;
    }

    @GetMapping("/{accountId}/transactions")
    public List<Transaction> getTransactionsByAccountId(@PathVariable Long accountId) {
        logger.info("Fetching transactions for account ID: {}", accountId);
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        logger.info("Fetched {} transactions for account ID: {}", transactions.size(), accountId);
        return transactions;
    }
}
