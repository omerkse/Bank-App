package com.ata.bankapp.controller;

import com.ata.bankapp.client.BillClient;
import com.ata.bankapp.exception.ResourceNotFoundException;
import com.ata.bankapp.model.Bill;
import com.ata.bankapp.model.BillStatus;
import com.ata.bankapp.repo.BillRepository;
import com.ata.bankapp.request.BillPaymentRequest;
import com.ata.bankapp.request.BillSearchRequest;
import com.ata.bankapp.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private static final Logger logger = LoggerFactory.getLogger(BillController.class);

    @Autowired
    private BillService billService;

    @Autowired
    private BillClient billClient;

    @Autowired
    private BillRepository billRepository;

    @PostMapping("/create")
    public Bill createBill(@RequestBody Bill bill) {
        logger.info("Creating bill: {}", bill);
        Bill createdBill = billClient.createBill(bill);
        logger.info("Bill created successfully with ID: {}", createdBill.getId());
        return createdBill;
    }

    @PostMapping("/pay")
    public ResponseEntity<?> payBill(@RequestBody BillPaymentRequest request) {
        logger.info("Paying bill with ID: {} using account ID: {} and amount: {}",
                request.getBillId(), request.getAccountId(), request.getAmount());

        Map<String, String> response = new HashMap<>();

        try {
            // Fatura ve hesap bilgilerini kontrol et
            Bill bill = billService.getBillById(request.getBillId());

            // Fatura durumunu kontrol et
            if (BillStatus.PAID.equals(bill.getStatus())) {
                response.put("message", "Bill is already paid.");
                return ResponseEntity.badRequest().body(response);
            }

            // Fatura tutarını kontrol et
            if (!bill.getAmount().equals(request.getAmount())) {
                response.put("message", "Invalid payment amount. Expected: " + bill.getAmount());
                return ResponseEntity.badRequest().body(response);
            }

            // Fatura hesap ID'sini kontrol et
            if (!bill.getAccountId().equals(request.getAccountId())) {
                response.put("message", "Bill does not belong to this account.");
                return ResponseEntity.badRequest().body(response);
            }

            // Ödeme işlemini gerçekleştir
            response = billService.payBill(
                    request.getAccountId(),
                    request.getBillId(),
                    request.getAmount()
            );

            logger.info("Bill with ID: {} paid successfully.", request.getBillId());
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            response.put("message", "Bill or account not found.");
            logger.error("Resource not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (RuntimeException e) {
            response.put("message", "Failed to pay the bill: " + e.getMessage());
            logger.error("Failed to pay bill with ID: {}. Error: {}",
                    request.getBillId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{billId}")
    public Bill getBillById(@PathVariable Long billId) {
        logger.info("Fetching bill with ID: {}", billId);
        Bill bill = billClient.getBillById(billId);
        logger.info("Fetched bill with ID: {}", billId);
        return bill;
    }

    @GetMapping
    public List<Bill> getAllBills() {
        logger.info("Fetching all bills");
        List<Bill> bills = billClient.getAllBills();
        logger.info("Fetched {} bills", bills.size());
        return bills;
    }

    @PostMapping("/markOverdue")
    public void markOverdueBills() {
        logger.info("Marking overdue bills");
        billClient.markOverdueBills();
        logger.info("Overdue bills marked successfully.");
    }
    @PostMapping("/search")
    public ResponseEntity<Bill> searchBill(@RequestBody BillSearchRequest request) {
        logger.info("Searching bill for account: {} and type: {}", request.getAccountId(), request.getBillType());

        Optional<Bill> bill = billRepository.findByAccountIdAndBillTypeAndStatus(
                request.getAccountId(),
                request.getBillType(),
                BillStatus.PENDING);

        if (bill.isPresent()) {
            return ResponseEntity.ok(bill.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
