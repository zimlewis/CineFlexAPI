package com.cineflex.API.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cineflex.API.model.Account;
import com.cineflex.API.model.UserPrincipal;
import com.cineflex.API.repository.AccountRepository;

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
    
}
