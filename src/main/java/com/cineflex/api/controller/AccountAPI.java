package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Account;
import com.cineflex.api.model.Subscription;
import com.cineflex.api.service.AccountDetailService;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.OrderService;

import java.net.http.HttpClient;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/users")
public class AccountAPI {

    private final AccountDetailService accountDetailService;
    private final OrderService orderService;
    private final AuthenticationService authenticationService;

    public AccountAPI (
        AccountDetailService accountDetailService,
        OrderService orderService,
        AuthenticationService authenticationService
    ) {
        this.accountDetailService = accountDetailService;
        this.orderService = orderService;
        this.authenticationService = authenticationService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Account> getUser(@PathVariable String id) {
        try {
            Account a = accountDetailService.getById(UUID.fromString(id));

            return new ResponseEntity<>(a, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    
    @GetMapping("/subscription")
    public ResponseEntity<Subscription> getUserSubscription(@RequestParam(required = false) List<String> ids) {
        try {
            Account a = authenticationService.getAccount();
            Subscription subscription = orderService.getUserSubscription(a.getId());

            if (subscription == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(subscription, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }


}
