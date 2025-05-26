package com.cineflex.API.controller;

import org.springframework.web.bind.annotation.RestController;
import com.cineflex.API.model.Show;
import com.cineflex.API.service.JsonService;
import com.cineflex.API.service.ShowService;
import com.fasterxml.jackson.databind.JsonNode;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;





@RestController
@RequestMapping("/api/show")
public class ShowAPI {

    private final ShowService showService;
    private final JsonService jsonService;

    // Inject show service
    public ShowAPI (
        ShowService showService,
        JsonService jsonService
    ) {
        this.showService = showService;
        this.jsonService = jsonService;
    }
    
    // Get single show database
    @GetMapping("/{id}")
    public ResponseEntity<Show> findShow(@PathVariable String id) {
        System.out.println(id);

        try {
            Show show = showService.findShowById(UUID.fromString(id));

            if (show == null) {
                
                return ResponseEntity
                    .of(ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND, // not found status code
                        "Cannot find show with given id" // error message
                    )) 
                    .build(); // Return an error if the id could not be found
            }

            return ResponseEntity.ok(show); // Return a show if the id is correct
        }
        catch (Exception e) {
            
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                e.getMessage()
            )).build();
        }
    }

    // Get all show
    @GetMapping("")
    public ResponseEntity<List<Show>> findAllShow() {

        try {
            List<Show> shows = showService.findAllShows();

            return ResponseEntity.ok(shows);
        }
        catch (Exception e) {
            // Return there's an exception
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                e.getMessage()
            )).build();
        }
    }
    

    // Add show to database
    @PostMapping("")
    public ResponseEntity<Show> addShow(@RequestBody JsonNode jsonNode) {
        try {
            // Get the show content from json node
            Show show = Show.builder()
                .title(jsonService.getOrNull(jsonNode, "title", String.class))
                .description(jsonService.getOrNull(jsonNode, "description", String.class))
                .releaseDate(jsonService.getOrNull(jsonNode, "releaseDate", LocalDate.class))
                .thumbnail(jsonService.getOrNull(jsonNode, "thumbnail", String.class))
                .onGoing(jsonService.getOrNull(jsonNode, "onGoing", Boolean.class))
                .isSeries(jsonService.getOrNull(jsonNode, "isSeries", Boolean.class))
                .build();
            
            Show returnShow = showService.addShow(show);

            return ResponseEntity.ok(returnShow);
        }
        catch (Exception e) {

            // Return error
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                e.getMessage()
            )).build();
        }

    }

    // Update show
    @PutMapping("/{id}")
    public ResponseEntity<Show> updateShow(@PathVariable String id, @RequestBody JsonNode jsonNode) {
        try {
            // Get the show content from json node
            Show show = Show.builder()
                .id(UUID.fromString(id))
                .title(jsonService.getOrNull(jsonNode, "title", String.class))
                .description(jsonService.getOrNull(jsonNode, "description", String.class))
                .releaseDate(jsonService.getOrNull(jsonNode, "releaseDate", LocalDate.class))
                .thumbnail(jsonService.getOrNull(jsonNode, "thumbnail", String.class))
                .onGoing(jsonService.getOrNull(jsonNode, "onGoing", Boolean.class))
                .isSeries(jsonService.getOrNull(jsonNode, "isSeries", Boolean.class))
                .build();
            
            Show returnShow = showService.updateShow(show);

            return ResponseEntity.ok(returnShow);
        }
        catch (Exception e) {

            // Return error
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                e.getMessage()
            )).build();
        }
    }

    // Delete show41a41207-b4c3-4832-ac4e-d9b6417a8ed4
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteShow(@PathVariable String id) {
        try {
            showService.deleteShow(UUID.fromString(id));

            return ResponseEntity.ok(null);
        }
        catch (Exception e) {
            // Return error
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                e.getMessage()
            )).build();
        }
    }
    
    

}
