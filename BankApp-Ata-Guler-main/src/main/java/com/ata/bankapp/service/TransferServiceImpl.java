package com.ata.bankapp.service;

import com.ata.bankapp.exception.ResourceNotFoundException;
import com.ata.bankapp.model.Account;
import com.ata.bankapp.model.Transfer;
import com.ata.bankapp.repo.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public void createTransfer(Transfer transfer) {
        logger.info("Creating transfer: {}", transfer);

        // Hesapları kontrol et
        validateAccounts(transfer.getFromAccountId(), transfer.getToAccountId(), transfer.getAmount());

        // Transfer tarihini set et
        transfer.setTransferDate(new java.sql.Timestamp(System.currentTimeMillis()));

        // Transferi kaydet
        transferRepository.save(transfer);

        // Hesap bakiyelerini güncelle
        updateAccountBalances(transfer);

        logger.info("Transfer completed successfully");
    }

    private void validateAccounts(Long fromAccountId, Long toAccountId, Double amount) {
        Account fromAccount = accountService.getAccount(fromAccountId);
        Account toAccount = accountService.getAccount(toAccountId);

        if (fromAccount == null) {
            throw new ResourceNotFoundException("Gönderen hesap bulunamadı");
        }
        if (toAccount == null) {
            throw new ResourceNotFoundException("Alıcı hesap bulunamadı");
        }
        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Yetersiz bakiye");
        }
    }

    private void updateAccountBalances(Transfer transfer) {
        Account fromAccount = accountService.getAccount(transfer.getFromAccountId());
        Account toAccount = accountService.getAccount(transfer.getToAccountId());

        // Bakiyeleri güncelle
        fromAccount.setBalance(fromAccount.getBalance() - transfer.getAmount());
        toAccount.setBalance(toAccount.getBalance() + transfer.getAmount());

        // Hesapları kaydet
        accountService.updateAccount(fromAccount.getId(), fromAccount);
        accountService.updateAccount(toAccount.getId(), toAccount);
    }

    @Override
    public Transfer getTransferById(Long transferId) {
        return transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));
    }

    @Override
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }
}