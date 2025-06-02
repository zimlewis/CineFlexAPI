package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cineflex.api.entity.UserPrincipal;
import com.cineflex.api.model.Account;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.JsonService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/authentication")
public class AuthenticationAPI {
    private final AuthenticationService authenticationService;
    private final JsonService jsonService;
    
    public AuthenticationAPI (
        AuthenticationService authenticationService,
        JsonService jsonService
    ) {
        this.authenticationService = authenticationService;
        this.jsonService = jsonService;
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
        catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, 
                e.getMessage()
            )).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody JsonNode jsonNode) {
        try {
            String email = jsonService.getOrNull(jsonNode, "email", String.class);
            String password = jsonService.getOrNull(jsonNode, "passowrd", String.class);
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
        catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, 
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
    
    
    
}
