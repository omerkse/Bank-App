package com.ata.bankapp;

import com.ata.bankapp.client.TransactionClient;
import com.ata.bankapp.controller.TransactionController;
import com.ata.bankapp.model.Transaction;
import com.ata.bankapp.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionClient transactionClient;

    private Transaction transaction;
    private List<Transaction> transactionList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(200.0);
        transaction.setDescription("Payment for invoice #123");

        transactionList = Arrays.asList(transaction);
    }

    @Test
    public void testCreateTransaction() throws Exception {
        given(transactionClient.createTransaction(transaction)).willReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":200.0,\"description\":\"Payment for invoice #123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetTransactionsByAccountId() throws Exception {
        given(transactionClient.getTransactionsByAccountId(1L)).willReturn(transactionList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions/account/{accountId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L));
    }

    @Test
    public void testGetTransactionById() throws Exception {
        given(transactionClient.getTransactionById(1L)).willReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions/{transactionId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }
}
