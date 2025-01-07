package com.ata.bankapp.service;

import com.ata.bankapp.model.Transaction;
import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    Transaction getTransactionById(Long id);
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsByAccountId(Long accountId);
}