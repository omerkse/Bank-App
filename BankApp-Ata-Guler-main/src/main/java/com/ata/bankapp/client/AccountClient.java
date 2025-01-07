package com.ata.bankapp.client;

import com.ata.bankapp.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "account-service", url = "http://localhost:8080/api/accounts")
public interface AccountClient {

    @GetMapping("/{accountId}")
    Account getAccount(@PathVariable("accountId") Long accountId);

    @GetMapping
    List<Account> getAllAccounts();
}
