package com.cineflex.API.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cineflex.API.model.Account;
import com.cineflex.API.repository.AccountRepository;

@Service
public class AuthenticationService {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthenticationService (
        AccountRepository accountRepository,
        AuthenticationManager authenticationManager,
        JwtService jwtService
    ) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public Account register(String username, String email, String password) {
        if (accountRepository.readAll().stream().anyMatch(a -> a.getUsername().equals(username))) {
            throw new RuntimeException("Username has already taken");
        }

        if (accountRepository.readAll().stream().anyMatch(a -> a.getEmail().equals(email))) {
            throw new RuntimeException("Email has already taken");
        }

        UUID id = UUID.randomUUID();

        Account account = Account.builder()
            .id(id)
            .username(username)
            .email(email)
            .password(encoder.encode(password))
            .createdTime(LocalDateTime.now())
            .updatedTime(LocalDateTime.now())
            .verify(true)
            .role(0)
            .build();
        
        try {
            accountRepository.create(account);
        }
        catch (Exception e) {
            throw e;
        }

        return accountRepository.read(id);
    }


    public String login(String email, String password) {
        Account account = accountRepository.readByEmail(email);
        String username = account != null?account.getUsername():null;

        if (username == null) {
            return null;
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Cannot login");
        }

        return jwtService.createToken(username);
    }
}
