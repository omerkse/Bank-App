package com.ata.bankapp;

import com.ata.bankapp.exception.ResourceNotFoundException;
import com.ata.bankapp.model.Account;
import com.ata.bankapp.model.Bill;
import com.ata.bankapp.model.BillStatus;
import com.ata.bankapp.repo.BillRepository;
import com.ata.bankapp.service.AccountService;
import com.ata.bankapp.service.BillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class BillServiceImplTest {

    @InjectMocks
    private BillServiceImpl billService;

    @Mock
    private BillRepository billRepository;

    @Mock
    private AccountService accountService;

    private Bill bill;
    private Account account;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        bill = new Bill();
        bill.setId(1L);
        bill.setAmount(500.0);
        bill.setStatus(BillStatus.PENDING);

        account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setBalance(1000.0);
    }

    @Test
    public void testCreateBill() {
        given(billRepository.save(bill)).willReturn(bill);

        Bill createdBill = billService.createBill(bill);

        assertNotNull(createdBill);
        assertEquals(bill.getId(), createdBill.getId());
        verify(billRepository, times(1)).save(bill);
    }

    @Test
    public void testPayBill() {
        Long accountId = 1L;
        Long billId = 1L;
        Double amount = 500.0;

        given(billRepository.findById(billId)).willReturn(Optional.of(bill));
        given(accountService.getFirstAccountByUserId(accountId)).willReturn(account);
        given(accountService.updateAccount(account.getId(), account)).willReturn(account);
        given(billRepository.save(bill)).willReturn(bill);

        Map<String, String> response = billService.payBill(accountId, billId, amount);

        assertNotNull(response);
        assertEquals("Bill paid successfully.", response.get("message"));
        assertEquals(BillStatus.PAID, bill.getStatus());
        verify(billRepository, times(1)).findById(billId);
        verify(accountService, times(1)).getFirstAccountByUserId(accountId);
        verify(accountService, times(1)).updateAccount(account.getId(), account);
        verify(billRepository, times(1)).save(bill);
    }

    @Test
    public void testPayBill_InsufficientAmount() {
        Long accountId = 1L;
        Long billId = 1L;
        Double amount = 200.0;

        given(billRepository.findById(billId)).willReturn(Optional.of(bill));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            billService.payBill(accountId, billId, amount);
        });

        assertEquals("Insufficient amount to pay the bill", thrown.getMessage());
        verify(billRepository, times(1)).findById(billId);
        verifyNoInteractions(accountService);
    }

    @Test
    public void testPayBill_InsufficientFunds() {
        Long accountId = 1L;
        Long billId = 1L;
        Double amount = 1500.0;

        given(billRepository.findById(billId)).willReturn(Optional.of(bill));
        given(accountService.getFirstAccountByUserId(accountId)).willReturn(account);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            billService.payBill(accountId, billId, amount);
        });

        assertEquals("Insufficient funds", thrown.getMessage());
        verify(billRepository, times(1)).findById(billId);
        verify(accountService, times(1)).getFirstAccountByUserId(accountId);
        verifyNoMoreInteractions(accountService);
        verifyNoInteractions(billRepository);
    }

    @Test
    public void testGetBillById() {
        given(billRepository.findById(1L)).willReturn(Optional.of(bill));

        Bill result = billService.getBillById(1L);

        assertNotNull(result);
        assertEquals(bill.getId(), result.getId());
        verify(billRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetBillById_NotFound() {
        given(billRepository.findById(1L)).willReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            billService.getBillById(1L);
        });

        assertEquals("Bill not found", thrown.getMessage());
        verify(billRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllBills() {
        given(billRepository.findAll()).willReturn(Arrays.asList(bill));

        List<Bill> bills = billService.getAllBills();

        assertNotNull(bills);
        assertEquals(1, bills.size());
        verify(billRepository, times(1)).findAll();
    }

    @Test
    public void testMarkOverdueBills() {
        Bill overdueBill = new Bill();
        overdueBill.setId(2L);
        overdueBill.setStatus(BillStatus.PENDING);

        given(billRepository.findAll()).willReturn(Arrays.asList(bill, overdueBill));
        given(billRepository.save(any(Bill.class))).willReturn(overdueBill);

        billService.markOverdueBills();

        assertEquals(BillStatus.OVERDUE, overdueBill.getStatus());
        verify(billRepository, times(2)).findAll();
        verify(billRepository, times(1)).save(overdueBill);
    }
}
