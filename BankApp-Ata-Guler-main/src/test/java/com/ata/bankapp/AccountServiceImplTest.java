package com.ata.bankapp;

import com.ata.bankapp.exception.ResourceNotFoundException;
import com.ata.bankapp.model.Account;
import com.ata.bankapp.model.AccountType;
import com.ata.bankapp.repo.AccountRepository;
import com.ata.bankapp.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setAccountType(AccountType.valueOf("SAVINGS"));
        account.setBalance(1000.0);
    }

    @Test
    public void testCreateAccount() {
        given(accountRepository.save(account)).willReturn(account);

        Account createdAccount = accountService.createAccount(account);

        assertNotNull(createdAccount);
        assertEquals(account.getId(), createdAccount.getId());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void testUpdateAccount() {
        Account updatedAccount = new Account();
        updatedAccount.setUserId(2L);
        updatedAccount.setAccountType(AccountType.valueOf("CHECKING"));
        updatedAccount.setBalance(2000.0);

        given(accountRepository.findById(1L)).willReturn(Optional.of(account));
        given(accountRepository.save(account)).willReturn(account);

        Account result = accountService.updateAccount(1L, updatedAccount);

        assertNotNull(result);
        assertEquals(updatedAccount.getUserId(), result.getUserId());
        assertEquals(updatedAccount.getAccountType(), result.getAccountType());
        assertEquals(updatedAccount.getBalance(), result.getBalance());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void testUpdateAccount_NotFound() {
        given(accountRepository.findById(1L)).willReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class,
                () -> accountService.updateAccount(1L, account));

        assertEquals("Account not found", thrown.getMessage());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllAccounts() {
        given(accountRepository.findAll()).willReturn(Arrays.asList(account));

        List<Account> accounts = accountService.getAllAccounts();

        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    public void testGetAccount() {
        given(accountRepository.findById(1L)).willReturn(Optional.of(account));

        Account result = accountService.getAccount(1L);

        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAccount_NotFound() {
        given(accountRepository.findById(1L)).willReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class,
                () -> accountService.getAccount(1L));

        assertEquals("Account not found", thrown.getMessage());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAccountsByUserId() {
        given(accountRepository.findByUserId(1L)).willReturn(Arrays.asList(account));

        List<Account> accounts = accountService.getAccountsByUserId(1L);

        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        verify(accountRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testGetFirstAccountByUserId() {
        given(accountRepository.findTopByUserId(1L)).willReturn(Optional.of(account));

        Account result = accountService.getFirstAccountByUserId(1L);

        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        verify(accountRepository, times(1)).findTopByUserId(1L);
    }

    @Test
    public void testGetFirstAccountByUserId_NotFound() {
        given(accountRepository.findTopByUserId(1L)).willReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class,
                () -> accountService.getFirstAccountByUserId(1L));

        assertEquals("No accounts found for user", thrown.getMessage());
        verify(accountRepository, times(1)).findTopByUserId(1L);
    }

    @Test
    public void testGetAccountBalance() {
        given(accountRepository.findById(1L)).willReturn(Optional.of(account));

        Double balance = accountService.getAccountBalance(1L);

        assertNotNull(balance);
        assertEquals(account.getBalance(), balance);
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAccountBalance_NotFound() {
        given(accountRepository.findById(1L)).willReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class,
                () -> accountService.getAccountBalance(1L));

        assertEquals("Account not found", thrown.getMessage());
        verify(accountRepository, times(1)).findById(1L);
    }
}
