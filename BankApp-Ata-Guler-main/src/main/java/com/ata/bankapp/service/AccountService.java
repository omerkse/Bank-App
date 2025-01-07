package com.ata.bankapp.service;

import com.ata.bankapp.model.Account;

import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    Account updateAccount(Long accountId, Account updatedAccount);
    List<Account> getAllAccounts();
    Account getAccount(Long accountId);
    List<Account> getAccountsByUserId(Long userId);
    Account getFirstAccountByUserId(Long userId);
    Double getAccountBalance(Long accountId);
}
