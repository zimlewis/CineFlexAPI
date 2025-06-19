package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.entity.UserPrincipal;
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

    public Account fromEmail(String email) {
        return accountRepository.readByEmail(email);
    }

    @Transactional
    public Account register(String username, String email, String password) {
        if (accountRepository.readAll().stream().anyMatch(a -> a.getUsername().equals(username))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username has already taken");
        }

        if (accountRepository.readAll().stream().anyMatch(a -> a.getEmail().equals(email))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email has already taken");
        }
        
        try {

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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public Account getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            return fromUsername(up.getUsername());
        }

        return null;
    }


    public String login(String email, String password) {
        Account account = accountRepository.readByEmail(email);
        String username = account != null?account.getUsername():null;

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email does not exist");
        }

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } 
        catch (LockedException e) {
            throw new ResponseStatusException(HttpStatus.LOCKED, e.getMessage());
        }
        catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        if (!authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot login");
        }


        return jwtService.createToken(username);
    }
}
