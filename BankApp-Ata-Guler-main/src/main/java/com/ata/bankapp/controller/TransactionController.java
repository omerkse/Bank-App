package com.ata.bankapp.controller;

import com.ata.bankapp.client.TransactionClient;
import com.ata.bankapp.model.Transaction;
import com.ata.bankapp.request.TransactionRequest;
import com.ata.bankapp.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        logger.info("Creating transaction: {}", transaction);
        try {
            Transaction createdTransaction = transactionService.createTransaction(transaction);
            logger.info("Transaction created successfully: {}", createdTransaction);
            return ResponseEntity.ok(createdTransaction);
        } catch (Exception e) {
            logger.error("Error creating transaction: ", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable Long accountId) {
        logger.info("Fetching transactions for account ID: {}", accountId);
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
}