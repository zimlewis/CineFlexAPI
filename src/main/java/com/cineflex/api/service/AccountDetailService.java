package com.cineflex.api.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.entity.UserPrincipal;
import com.cineflex.api.model.Account;
import com.cineflex.api.repository.AccountRepository;

@Service
public class AccountDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountDetailService (
        AccountRepository accountRepository
    ) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.readByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        UserDetails userDetails = new UserPrincipal(account);

        return userDetails;
    }

    public Account getById(UUID id) {
        try {
            Account account = accountRepository.read(id);
            account.setPassword(null);

            return account;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
}
