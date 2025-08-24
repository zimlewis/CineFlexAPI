package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Advertisement;
import com.cineflex.api.service.AdvertisementService;
import com.cineflex.api.service.JsonService;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementAPI {
    private final JsonService jsonService;
    private final AdvertisementService advertisementService;

    public AdvertisementAPI (
        JsonService jsonService,
        AdvertisementService advertisementService
    ) {
        this.jsonService = jsonService;
        this.advertisementService = advertisementService;
    }

    @GetMapping("")
    public ResponseEntity<List<Advertisement>> findPaginatedAdvertisement(
        @RequestParam(required = false, defaultValue = "0") Integer page, 
        @RequestParam(required = false, defaultValue = "5") Integer size
    ) {
        try {
            List<Advertisement> advertisements = advertisementService.getPaginatedAdvertisement(page, size);

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", advertisementService.getAdvertisementPageCount(size).toString());

            ResponseEntity<List<Advertisement>> response = new ResponseEntity<>(advertisements, headers, HttpStatus.OK);

            return response;
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{type}/random")
    public ResponseEntity<Advertisement> getRandomAds(@PathVariable Integer type) {
        try {
            Advertisement advertisement = advertisementService.getRandomTypeAdvertisement(type);

            if (advertisement == null) {

                String img = new String[] {"https://placehold.co/728x90", "https://placehold.co/600x400", "https://placehold.co/600x400"}[type];

                advertisement = Advertisement.builder()
                    .createdTime(LocalDateTime.now())
                    .updatedTime(LocalDateTime.now())
                    .link("https://example.com/")
                    .image(img)
                    .enabled(true)
                    .type(type)
                    .build();
            }

            return new ResponseEntity<>(advertisement, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }

    }

    @GetMapping("/dash/total")
    public ResponseEntity<Integer> getAdvertisementCount() {
        try {
            Integer count = advertisementService.getTotalAdvertisementCount();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return ResponseEntity.of(
                    ProblemDetail.forStatusAndDetail(
                            e.getStatusCode(),
                            e.getReason()
                    )
            ).build();
        }
    }
    
    
    
    @PostMapping("")
    public ResponseEntity<Advertisement> createAdvertisement(@RequestBody JsonNode body) {
        try {
            Advertisement bodyAdvertisement = Advertisement.builder()
                .link(jsonService.getOrNull(body, "link", String.class))
                .enabled(true)
                .image(jsonService.getOrNull(body, "image", String.class))
                .type(jsonService.getOrNull(body, "type", Integer.class))
                .hirer(UUID.fromString(jsonService.getOrNull(body, "hirer", String.class)))
                .build();
            
            System.out.println(bodyAdvertisement);
            Advertisement responseAdvertisement = advertisementService.addAdvertisement(bodyAdvertisement);
            return new ResponseEntity<>(responseAdvertisement, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Advertisement> updateAdvertisement(@RequestBody JsonNode body, @PathVariable String id) {
        try {
            Advertisement bodyAdvertisement = Advertisement.builder()
                .id(UUID.fromString(id))
                .link(jsonService.getOrNull(body, "link", String.class))
                .enabled(jsonService.getOrNull(body, "enabled", Boolean.class))
                .image(jsonService.getOrNull(body, "image", String.class))
                .type(jsonService.getOrNull(body, "type", Integer.class))
                .hirer(UUID.fromString(jsonService.getOrNull(body, "hirer", String.class)))
                .build();
            
            Advertisement responseAdvertisement = advertisementService.updateAdvertisement(bodyAdvertisement);
            return new ResponseEntity<>(responseAdvertisement, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }
    
}
