package com.ata.bankapp.request;

import com.ata.bankapp.model.BillStatus;
import com.ata.bankapp.model.BillType;
import lombok.Data;

@Data
public class BillSearchRequest {
    private Long accountId;
    private BillType billType;
    private BillStatus status;
}