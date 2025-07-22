package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    public Account unbanAccount(UUID id) {
        try {
            Account account = accountRepository.read(id);
            account.setActivate(true);

            accountRepository.update(id, account);
            
            return accountRepository.read(id); // Return the updated show
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Account banAccount(UUID id) {
        try {
            Account account = accountRepository.read(id);
            account.setActivate(false);

            accountRepository.update(id, account);
            
            return accountRepository.read(id); // Return the updated show
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Account updateAccount(Account account) {
        try {
            UUID id = account.getId(); // Get the id from show to update
            Account oldAccount = accountRepository.read(id);


            account.setUsername(Objects.requireNonNullElse(account.getUsername(), oldAccount.getUsername()));
            account.setEmail(Objects.requireNonNullElse(account.getEmail(), oldAccount.getEmail()));
            account.setPassword(Objects.requireNonNullElse(account.getPassword(), oldAccount.getPassword()));
            account.setCreatedTime(oldAccount.getCreatedTime());
            account.setUpdatedTime(LocalDateTime.now());
            account.setVerify(oldAccount.getVerify());
            account.setRole(Objects.requireNonNullElse(account.getRole(), oldAccount.getRole()));
            account.setActivate(oldAccount.getActivate());

            accountRepository.update(id, account);
            
            return accountRepository.read(id); // Return the updated show
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
