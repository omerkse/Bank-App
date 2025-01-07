package com.ata.bankapp.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType type;

    private String description;

    @Column(name = "related_account_id")
    private Long relatedAccountId;

    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;  // field adının düzgün olduğundan emin oluyoruz

    @PrePersist
    protected void onCreate() {
        if (transactionDate == null) {
            transactionDate = LocalDateTime.now();
        }
    }
}