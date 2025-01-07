package com.ata.bankapp.client;

import com.ata.bankapp.model.Transfer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "transfer-service", url = "http://localhost:8080/api/transfers")
public interface TransferClient {

    @PostMapping
    void createTransfer(@RequestBody Transfer transfer);

    @GetMapping("/{transferId}")
    Transfer getTransferById(@PathVariable("transferId") Long transferId);

    @GetMapping
    List<Transfer> getAllTransfers();
}
