package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.dto.BankTransfer;
import com.cineflex.api.model.BillingDetail;
import com.cineflex.api.service.OrderService;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/orders")
public class OrderAPI {

    private final OrderService orderService;

    public OrderAPI (
        OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingDetail> getById(@PathVariable String id) {
        try {
            BillingDetail b = orderService.getOrderById(UUID.fromString(id));

            return new ResponseEntity<>(b, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getStatusCode(), e.getReason())).build();
        }
    }
    

    @PostMapping("")
    public ResponseEntity<BillingDetail> createOrder() {
        try {
            BillingDetail b = orderService.createOrder(10000.0);

            return new ResponseEntity<>(b, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmOrder(@RequestBody BankTransfer entity) {
        try {
            orderService.confirmPayment(entity);;

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }
    
    
}
