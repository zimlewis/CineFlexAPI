package com.cineflex.API.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cineflex.API.model.Episode;
import com.cineflex.API.model.Season;
import com.cineflex.API.service.JsonService;
import com.cineflex.API.service.ShowService;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/seasons")
public class SeasonAPI {
    private final ShowService showService;
    private final JsonService jsonService;


    // Inject show service
    public SeasonAPI (
        ShowService showService,
        JsonService jsonService
    ) {
        this.showService = showService;
        this.jsonService = jsonService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Season> getSeasonById(@PathVariable String id) {
        try {
            Season season = showService.findSeasonById(UUID.fromString(id));

            if (season == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND, 
                    "Cannot find the season with given id"
                )).build();
            }

            return new ResponseEntity<Season>(season, HttpStatus.OK);

        }
        catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                e.getMessage()
            )).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Season> updateSeason(@PathVariable String id, @RequestBody JsonNode jsonNode) {
        try {
            Season season = Season.builder()
                .id(UUID.fromString(id))
                .title(jsonService.getOrNull(jsonNode, "title", String.class))
                .releaseDate(LocalDate.parse(jsonService.getOrNull(jsonNode, "releaseDate", String.class)))
                .description(jsonService.getOrNull(jsonNode, "description", String.class))
                .show(UUID.fromString(jsonService.getOrNull(jsonNode, "show", String.class)))
                .build();
            
            Season returnSeason = showService.updateSeason(season);

            return new ResponseEntity<Season>(returnSeason, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                e.getMessage()
            )).build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeason(@PathVariable String id) {
        try {
            showService.deleteSeason(UUID.fromString(id));

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            // Return error
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                e.getMessage()
            )).build();
        }
    }

    @PostMapping("/{id}/episodes")
    public ResponseEntity<Episode> addEpisode(@RequestBody JsonNode jsonNode) {
        //TODO: process POST request
        
        return null;
    }
    
    
}
