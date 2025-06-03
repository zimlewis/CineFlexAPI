package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Season;
import com.cineflex.api.model.Show;
import com.cineflex.api.service.JsonService;
import com.cineflex.api.service.ShowService;
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
@RequestMapping("/api/shows")
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
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
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
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
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
                .releaseDate(jsonNode.has("releaseDate")?LocalDate.parse(jsonNode.get("releaseDate").asText()):null)
                .thumbnail(jsonService.getOrNull(jsonNode, "thumbnail", String.class))
                .onGoing(jsonService.getOrNull(jsonNode, "onGoing", Boolean.class))
                .isSeries(jsonService.getOrNull(jsonNode, "isSeries", Boolean.class))
                .ageRating(jsonService.getOrNull(jsonNode, "ageRating", String.class))
                .build();
            
            Show returnShow = showService.addShow(show);

            return new ResponseEntity<Show>(returnShow, HttpStatus.CREATED);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
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
                .releaseDate(jsonNode.has("releaseDate")?LocalDate.parse(jsonNode.get("releaseDate").asText()):null)
                .thumbnail(jsonService.getOrNull(jsonNode, "thumbnail", String.class))
                .onGoing(jsonService.getOrNull(jsonNode, "onGoing", Boolean.class))
                .isSeries(jsonService.getOrNull(jsonNode, "isSeries", Boolean.class))
                .ageRating(jsonService.getOrNull(jsonNode, "ageRating", String.class))
                .build();
            
            Show returnShow = showService.updateShow(show);

            return ResponseEntity.ok(returnShow);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getMessage()
            )).build();
        }
    }

    // Delete show
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShow(@PathVariable String id) {
        try {
            showService.deleteShow(UUID.fromString(id));

            return ResponseEntity.ok(null);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getMessage()
            )).build();
        }
    }

    @GetMapping("/{id}/seasons")
    public ResponseEntity<List<Season>> getSeasonsOfAShow(@PathVariable String id) {
        return ResponseEntity.ok().body(showService.findSeaonsByShows(UUID.fromString(id)));
    }
    
    @PostMapping("/{id}/seasons")
    public ResponseEntity<Season> addSeasonToShowShowAPI(@PathVariable String id, @RequestBody JsonNode jsonNode) {
        try {
            Season season = Season.builder()
                .title(jsonService.getOrNull(jsonNode, "title", String.class))
                .releaseDate(jsonNode.has("releaseDate")?LocalDate.parse(jsonNode.get("releaseDate").asText()):null)
                .description(jsonService.getOrNull(jsonNode, "description", String.class))
                .show(UUID.fromString(id))
                .build();
            
            Season returnSeason = showService.addSeason(season);

            return new ResponseEntity<Season>(returnSeason, HttpStatus.CREATED);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getMessage()
            )).build();
        }
    }
    
    

}
