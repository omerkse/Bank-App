package com.ata.bankapp.repo;

import com.ata.bankapp.model.Transaction;
import com.ata.bankapp.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);
    List<Transaction> findByAccountIdAndType(Long accountId, TransactionType type);
    List<Transaction> findByAccountIdAndTransactionDateBetween(Long accountId, LocalDateTime start, LocalDateTime end);  // createdAt yerine transactionDate kullanıyoruz
}