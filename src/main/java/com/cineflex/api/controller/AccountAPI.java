package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Account;
import com.cineflex.api.service.AccountDetailService;

import java.net.http.HttpClient;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/users")
public class AccountAPI {

    private final AccountDetailService accountDetailService;

    public AccountAPI (
        AccountDetailService accountDetailService
    ) {
        this.accountDetailService = accountDetailService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Account> getUser(@PathVariable String id) {
        try {
            Account a =accountDetailService.getById(UUID.fromString(id));

            return new ResponseEntity<>(a, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    

}
