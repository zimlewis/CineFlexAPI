package com.cineflex.api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Account;
import com.cineflex.api.repository.AccountRepository;

@Service
public class AccountModeratingService {
    private final AccountRepository accountRepository;


    public AccountModeratingService (
        AccountRepository accountRepository
    ) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAccounts(Integer page, Integer size) {
        try {
            List<Account> accounts = accountRepository.readAll(page, size);

            return accounts;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Integer getAccountsPageCount(Integer size) {
        try {
            return accountRepository.getPageCount(size);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
