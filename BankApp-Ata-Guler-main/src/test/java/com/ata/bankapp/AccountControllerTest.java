package com.ata.bankapp;

import com.ata.bankapp.client.AccountClient;
import com.ata.bankapp.controller.AccountController;
import com.ata.bankapp.model.Account;
import com.ata.bankapp.model.AccountType;
import com.ata.bankapp.model.Transaction;
import com.ata.bankapp.security.MyUserDetailsService;
import com.ata.bankapp.service.AccountService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private AccountClient accountClient;

    @MockBean
    private MyUserDetailsService myUserDetailsService; // MyUserDetailsService bean'i ekleniyor

    private Account account;
    private List<Account> accountList;
    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(100.0);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccountId(1L);
        transaction.setAmount(100.0);

        accountList = Arrays.asList(account);
    }

    @Test
    public void testCreateAccount() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account created successfully.");

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"accountType\":\"SAVINGS\",\"balance\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Account created successfully."));
    }

    @Test
    public void testUpdateAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/accounts/{accountId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"accountType\":\"CHECKING\",\"balance\":200.0}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Account updated successfully."));
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        given(accountClient.getAllAccounts()).willReturn(accountList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountType").value("SAVINGS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance").value(100.0));
    }

    @Test
    public void testGetAccount() throws Exception {
        given(accountClient.getAccount(1L)).willReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/{accountId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(100.0));
    }

    @Test
    public void testGetAccountsByUserId() throws Exception {
        given(accountService.getAccountsByUserId(1L)).willReturn(accountList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/user/{userId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountType").value("SAVINGS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance").value(100.0));
    }

    @Test
    public void testGetFirstAccountByUserId() throws Exception {
        given(accountService.getFirstAccountByUserId(1L)).willReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/user/{userId}/first", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(100.0));
    }

    @Test
    public void testGetAccountBalance() throws Exception {
        given(accountService.getAccountBalance(1L)).willReturn(100.0);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/{accountId}/balance", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(100.0));
    }

    @Test
    public void testGetTransactionsByAccountId() throws Exception {
        given(transactionService.getTransactionsByAccountId(1L)).willReturn(Arrays.asList(transaction));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/{accountId}/transactions", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(100.0));
    }
}
