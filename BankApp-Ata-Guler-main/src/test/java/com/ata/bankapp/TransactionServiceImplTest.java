package com.ata.bankapp;

import com.ata.bankapp.exception.ResourceNotFoundException;
import com.ata.bankapp.model.Transaction;
import com.ata.bankapp.repo.TransactionRepository;
import com.ata.bankapp.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccountId(1L);
        transaction.setAmount(100.0);
        transaction.setDescription("Sample Transaction");
    }

    @Test
    public void testCreateTransaction() {
        given(transactionRepository.save(transaction)).willReturn(transaction);

        Transaction createdTransaction = transactionService.createTransaction(transaction);

        assertNotNull(createdTransaction);
        assertEquals(transaction.getId(), createdTransaction.getId());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void testGetTransactionById() {
        given(transactionRepository.findById(1L)).willReturn(Optional.of(transaction));

        Transaction result = transactionService.getTransactionById(1L);

        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTransactionById_NotFound() {
        given(transactionRepository.findById(1L)).willReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getTransactionById(1L);
        });

        assertEquals("Transaction not found", thrown.getMessage());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllTransactions() {
        given(transactionRepository.findAll()).willReturn(Arrays.asList(transaction));

        List<Transaction> transactions = transactionService.getAllTransactions();

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(transaction.getId(), transactions.get(0).getId());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    public void testGetTransactionsByAccountId() {
        Long accountId = 1L;
        given(transactionRepository.findByAccountId(accountId)).willReturn(Arrays.asList(transaction));

        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(accountId, transactions.get(0).getAccountId());
        verify(transactionRepository, times(1)).findByAccountId(accountId);
    }
}
