package com.ata.bankapp.client;

import com.ata.bankapp.model.Bill;
import com.ata.bankapp.request.BillPaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "bill-service", url = "http://localhost:8080/api/bills")
public interface BillClient {

    @PostMapping("/create")
    Bill createBill(@RequestBody Bill bill);

    @PostMapping("/pay")
    Map<String, String> payBill(@RequestBody BillPaymentRequest request);

    @GetMapping("/{billId}")
    Bill getBillById(@PathVariable("billId") Long billId);

    @GetMapping
    List<Bill> getAllBills();

    @PostMapping("/markOverdue")
    void markOverdueBills();
}
