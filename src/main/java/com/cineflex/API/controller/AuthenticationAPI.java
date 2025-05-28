package com.cineflex.API.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cineflex.API.model.Account;
import com.cineflex.API.service.AuthenticationService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/authentication")
public class AuthenticationAPI {
    private final AuthenticationService authenticationServide;
    
    public AuthenticationAPI (
        AuthenticationService authenticationService
    ) {
        this.authenticationServide = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody JsonNode jsonNode) {
        try {
            String token = authenticationServide.login(jsonNode.get("email").asText(), jsonNode.get("password").asText());

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
            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();
            String username = jsonNode.get("username").asText();

            Account account = authenticationServide.register(username, email, password);

            return new ResponseEntity<>(account, HttpStatus.ACCEPTED);

        }
        catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, 
                e.getMessage()
            )).build();
        }
    }
    
    
}
