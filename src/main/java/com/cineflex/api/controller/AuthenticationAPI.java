package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.entity.UserPrincipal;
import com.cineflex.api.model.Account;
import com.cineflex.api.model.VerificationToken;
import com.cineflex.api.service.AccountDetailService;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.JsonService;
import com.cineflex.api.service.TokenService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/authentication")
public class AuthenticationAPI {

    private final AccountDetailService accountDetailService;
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final JsonService jsonService;
    
    public AuthenticationAPI (
        AuthenticationService authenticationService,
        JsonService jsonService,
        TokenService tokenService, 
        AccountDetailService accountDetailService
    ) {
        this.authenticationService = authenticationService;
        this.jsonService = jsonService;
        this.tokenService = tokenService;
        this.accountDetailService = accountDetailService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody JsonNode jsonNode) {
        try {
            String email = jsonService.getOrNull(jsonNode, "email", String.class);
            String password = jsonService.getOrNull(jsonNode, "password", String.class);
            
            if (email == null || password == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.UNAUTHORIZED, 
                    "Cannot login"
                )).build();
            }

            String token = authenticationService.login(email, password);

            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getMessage()
            )).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody JsonNode jsonNode) {
        try {
            String email = jsonService.getOrNull(jsonNode, "email", String.class);
            String password = jsonService.getOrNull(jsonNode, "password", String.class);
            String username = jsonService.getOrNull(jsonNode, "username", String.class);
            
            if (email == null || password == null || username == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.UNAUTHORIZED, 
                    "Cannot register"
                )).build();
            }

            Account account = authenticationService.register(username, email, password);

            return new ResponseEntity<>(account, HttpStatus.ACCEPTED);

        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getMessage()
            )).build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Account> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            return new ResponseEntity<>(authenticationService.fromUsername(up.getUsername()), HttpStatus.OK);
        }

        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED, 
            "request is not authenticated"
        )).build();
    }
    
    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verify(@PathVariable String token) {
        try {
            tokenService.verifyToken(token);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getMessage()
            )).build();
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<?> sendVerifyEmail(@RequestBody JsonNode jsonNode) {
        String email = jsonService.getOrNull(jsonNode, "email", String.class);

        
        if (email == null) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_ACCEPTABLE, 
                "Email cannot be null"
            )).build();
        }

        Account account = authenticationService.fromEmail(email);


        if (account == null) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, 
                "Email not found"
            )).build();
        }

        if (account.getVerify()) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, 
                "This account has verified already"
            )).build();
        }


        VerificationToken token = tokenService.getAvailableToken(account);

        if (token != null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        try {
            tokenService.createToken(account);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getMessage()
            )).build();
        }
    }
    
    
}
