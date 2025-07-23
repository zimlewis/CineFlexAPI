package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Hirer;
import com.cineflex.api.model.Show;
import com.cineflex.api.service.AdvertisementService;
import com.cineflex.api.service.JsonService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.websocket.server.PathParam;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/hirers")
public class HirerAPI {

    private final AdvertisementService advertisementService;
    private final JsonService jsonService;

    public HirerAPI (
        AdvertisementService advertisementService,
        JsonService jsonService
    ) {
        this.advertisementService = advertisementService;
        this.jsonService = jsonService;
    }

    @PostMapping("")
    public ResponseEntity<Hirer> createNewHirer(@RequestBody JsonNode body) {
        try {
            Hirer hirer = Hirer.builder()
                .alias(jsonService.getOrNull(body, "alias", String.class))
                .email(jsonService.getOrNull(body, "email", String.class))
                .phone(jsonService.getOrNull(body, "phone", String.class))
                .build();
            
            Hirer returned = advertisementService.addHirer(hirer);
            return new ResponseEntity<>(returned, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hirer> getHirer(
        @PathVariable String id
    ) {
        try {
            Hirer hirer = advertisementService.getHirer(UUID.fromString(id));

            return new ResponseEntity<>(hirer, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    
    @GetMapping("")
    public ResponseEntity<List<Hirer>> getPaginatedHirer(
        @RequestParam(required = false, defaultValue = "0") Integer page, 
        @RequestParam(required = false, defaultValue = "100") Integer size
    ) {
        try {
            List<Hirer> hirers;

            hirers = advertisementService.getPaginatedHirer(page, size);

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", advertisementService.getHirerPage(size).toString()); 

            ResponseEntity<List<Hirer>> responseEntity = new ResponseEntity<List<Hirer>>(hirers, headers, HttpStatus.OK);

            return responseEntity;  
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    
}
