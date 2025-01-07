package com.ata.bankapp.request;

import com.ata.bankapp.model.TransactionType;
import lombok.Data;

@Data
public class TransactionRequest {
    private Long accountId;
    private Double amount;
    private TransactionType type;
    private String description;
    private Long relatedAccountId;
    private Long billId;
    private String timestamp;
}