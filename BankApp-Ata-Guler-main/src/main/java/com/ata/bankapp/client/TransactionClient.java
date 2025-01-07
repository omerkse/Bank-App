package com.ata.bankapp.client;

import com.ata.bankapp.model.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "transaction-service", url = "http://localhost:8080/api/transactions")
public interface TransactionClient {

    @PostMapping("/create")
    Transaction createTransaction(@RequestBody Transaction transaction);

    @GetMapping("/account/{accountId}")
    List<Transaction> getTransactionsByAccountId(@PathVariable("accountId") Long accountId);

    @GetMapping("/{transactionId}")
    Transaction getTransactionById(@PathVariable("transactionId") Long transactionId);
}
