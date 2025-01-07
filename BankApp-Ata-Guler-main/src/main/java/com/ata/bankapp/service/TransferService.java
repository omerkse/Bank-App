package com.ata.bankapp.service;

import com.ata.bankapp.model.Transfer;
import java.util.List;

public interface TransferService {
    void createTransfer(Transfer transfer);
    Transfer getTransferById(Long transferId);
    List<Transfer> getAllTransfers();
}
