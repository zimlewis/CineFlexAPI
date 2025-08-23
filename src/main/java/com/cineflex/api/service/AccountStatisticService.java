package com.cineflex.api.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.repository.AccountRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountStatisticService {

    private final AccountRepository accountRepository;

    public AccountStatisticService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Map<String, Object> getUserStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", accountRepository.countAllUsers());
            stats.put("freeUsers", accountRepository.countFreeUsers());
            stats.put("activeSubscriptions", accountRepository.countActiveSubscriptions());
            return stats;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public long getTotalUsers() {
        return accountRepository.countAllUsers();
    }

    public long getFreeUsers() {
        return accountRepository.countFreeUsers();
    }

    public long getActiveSubscriptions() {
        return accountRepository.countActiveSubscriptions();
    }
}
