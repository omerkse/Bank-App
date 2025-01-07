package com.ata.bankapp.service;

import com.ata.bankapp.exception.ResourceNotFoundException;
import com.ata.bankapp.model.Account;
import com.ata.bankapp.model.Bill;
import com.ata.bankapp.model.BillStatus;
import com.ata.bankapp.repo.BillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public Bill createBill(Bill bill) {
        logger.info("Creating bill: {}", bill);
        return billRepository.save(bill);
    }

    @Override
    public Map<String, String> payBill(Long accountId, Long billId, Double amount) {
        logger.info("Paying bill with ID: {} for account ID: {} with amount: {}", billId, accountId, amount);
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> {
                    logger.error("Bill not found with ID: {}", billId);
                    return new ResourceNotFoundException("Bill not found");
                });

        if (bill.getAmount() > amount) {
            logger.error("Insufficient amount to pay the bill. Required: {}, Provided: {}", bill.getAmount(), amount);
            throw new RuntimeException("Insufficient amount to pay the bill");
        }

        Account account = accountService.getFirstAccountByUserId(accountId);

        if (account.getBalance() < amount) {
            logger.error("Insufficient funds for account ID: {}. Available: {}, Required: {}", accountId, account.getBalance(), amount);
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);
        accountService.updateAccount(account.getId(), account);

        bill.setStatus(BillStatus.PAID);
        billRepository.save(bill);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Bill paid successfully.");
        logger.info("Bill paid successfully with ID: {}", billId);
        return response;
    }

    @Override
    public Bill getBillById(Long billId) {
        logger.info("Fetching bill with ID: {}", billId);
        return billRepository.findById(billId)
                .orElseThrow(() -> {
                    logger.error("Bill not found with ID: {}", billId);
                    return new ResourceNotFoundException("Bill not found");
                });
    }

    @Override
    public List<Bill> getAllBills() {
        logger.info("Fetching all bills");
        return billRepository.findAll();
    }

    @Override
    public void markOverdueBills() {
        logger.info("Marking overdue bills");
        List<Bill> bills = billRepository.findAll();
        bills.forEach(bill -> {
            if (isOverdue(bill)) {
                bill.setStatus(BillStatus.OVERDUE);
                billRepository.save(bill);
                logger.info("Bill marked as overdue with ID: {}", bill.getId());
            }
        });
    }

    private boolean isOverdue(Bill bill) {
        return BillStatus.PENDING.equals(bill.getStatus());
    }
}
