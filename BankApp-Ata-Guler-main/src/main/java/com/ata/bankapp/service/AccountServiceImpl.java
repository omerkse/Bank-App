package com.ata.bankapp.service;

import com.ata.bankapp.exception.ResourceNotFoundException;
import com.ata.bankapp.model.Account;
import com.ata.bankapp.repo.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        logger.info("Creating account: {}", account);
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Long accountId, Account updatedAccount) {
        logger.info("Updating account with ID: {}", accountId);
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        existingAccount.setUserId(updatedAccount.getUserId());
        existingAccount.setAccountType(updatedAccount.getAccountType());
        existingAccount.setBalance(updatedAccount.getBalance());
        Account updated = accountRepository.save(existingAccount);
        logger.info("Account updated successfully: {}", updated);
        return updated;
    }

    @Override
    public List<Account> getAllAccounts() {
        logger.info("Fetching all accounts");
        return accountRepository.findAll();
    }

    @Override
    public Account getAccount(Long accountId) {
        logger.info("Fetching account with ID: {}", accountId);
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        logger.info("Fetching accounts for user ID: {}", userId);
        return accountRepository.findByUserId(userId);
    }

    @Override
    public Account getFirstAccountByUserId(Long userId) {
        logger.info("Fetching first account for user ID: {}", userId);
        return accountRepository.findTopByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No accounts found for user"));
    }

    @Override
    public Double getAccountBalance(Long accountId) {
        logger.info("Fetching balance for account ID: {}", accountId);
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"))
                .getBalance();
    }
}
