package com.ata.bankapp.service;

import com.ata.bankapp.model.Bill;

import java.util.List;
import java.util.Map;

public interface BillService {
    Bill createBill(Bill bill);
    Map<String, String> payBill(Long accountId, Long billId, Double amount);
    Bill getBillById(Long billId);
    List<Bill> getAllBills();
    void markOverdueBills();
}
