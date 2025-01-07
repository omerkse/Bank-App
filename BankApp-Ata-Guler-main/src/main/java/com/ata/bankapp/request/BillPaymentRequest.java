package com.ata.bankapp.request;

import lombok.Data;

@Data
public class BillPaymentRequest {
    private Long accountId;
    private Long billId;
    private Double amount;
}
