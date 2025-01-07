package com.ata.bankapp.service;

import com.ata.bankapp.model.Transaction;
import com.ata.bankapp.repo.TransactionRepository;
import com.ata.bankapp.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long id) {
        logger.info("Fetching transaction with ID: {}", id);
        return transactionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Transaction not found with ID: {}", id);
                    return new ResourceNotFoundException("Transaction not found");
                });
    }

    @Override
    public List<Transaction> getAllTransactions() {
        logger.info("Fetching all transactions");
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        logger.info("Fetching transactions for account ID: {}", accountId);
        return transactionRepository.findByAccountId(accountId);
    }

}