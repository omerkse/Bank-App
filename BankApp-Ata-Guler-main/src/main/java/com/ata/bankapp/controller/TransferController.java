package com.ata.bankapp.controller;

import com.ata.bankapp.exception.ResourceNotFoundException;
import com.ata.bankapp.model.Transfer;
import com.ata.bankapp.request.TransferRequest;
import com.ata.bankapp.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    private TransferService transferService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createTransfer(@RequestBody TransferRequest transferRequest) {
        try {
            logger.info("Initiating transfer: {}", transferRequest);

            Transfer transfer = new Transfer();
            transfer.setFromAccountId(transferRequest.getFromAccountId());
            transfer.setToAccountId(transferRequest.getToAccountId());
            transfer.setAmount(transferRequest.getAmount());
            transfer.setTransferDate(new java.sql.Timestamp(System.currentTimeMillis())); // Tarih eklendi

            transferService.createTransfer(transfer);

            logger.info("Transfer completed successfully: {}", transferRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Transfer completed successfully.");
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            logger.error("Transfer failed: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            logger.error("Transfer failed: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{transferId}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable Long transferId) {
        logger.info("Fetching transfer with ID: {}", transferId);
        Transfer transfer = transferService.getTransferById(transferId);
        logger.info("Fetched transfer with ID: {}", transferId);
        return ResponseEntity.ok(transfer);
    }

    @GetMapping
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        logger.info("Fetching all transfers");
        List<Transfer> transfers = transferService.getAllTransfers();
        logger.info("Fetched {} transfers", transfers.size());
        return ResponseEntity.ok(transfers);
    }
}
