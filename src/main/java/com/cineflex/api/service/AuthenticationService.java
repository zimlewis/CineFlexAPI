package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cineflex.api.model.Account;
import com.cineflex.api.repository.AccountRepository;

@Service
public class AuthenticationService {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;


    public AuthenticationService (
        AccountRepository accountRepository,
        AuthenticationManager authenticationManager,
        JwtService jwtService,
        TokenService tokenService
    ) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }

    public Account fromUsername(String username) {
        return accountRepository.readByUsername(username);
    }

    @Transactional
    public Account register(String username, String email, String password) {

        
        try {
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
                .verify(false)
                .role(0)
                .build();

            accountRepository.create(account);

            tokenService.createToken(account);

            return accountRepository.read(id);
        }
        catch (Exception e) {
            throw e;
        }

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
