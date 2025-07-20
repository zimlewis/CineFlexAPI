package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Episode;
import com.cineflex.api.model.Season;
import com.cineflex.api.service.JsonService;
import com.cineflex.api.service.ShowService;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.HttpHeadResponseDecorator;
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
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Season> updateSeason(@PathVariable String id, @RequestBody JsonNode jsonNode) {
        try {
            UUID updateId = jsonService.getOrNull(jsonNode, "show", String.class) == null ? null : UUID.fromString(jsonService.getOrNull(jsonNode, "show", String.class));

            Season season = Season.builder()
                .id(UUID.fromString(id))
                .title(jsonService.getOrNull(jsonNode, "title", String.class))
                .releaseDate(jsonNode.has("releaseDate")?LocalDate.parse(jsonNode.get("releaseDate").asText()):null)
                .description(jsonService.getOrNull(jsonNode, "description", String.class))
                .show(updateId)
                .build();
            
            Season returnSeason = showService.updateSeason(season);

            return new ResponseEntity<Season>(returnSeason, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeason(@PathVariable String id) {
        try {
            showService.deleteSeason(UUID.fromString(id));

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{id}/episodes")
    public ResponseEntity<List<Episode>> getAllEpisodesFromSeason(
        @RequestParam(required = false, defaultValue = "0") Integer page, 
        @RequestParam(required = false, defaultValue = "100") Integer size, 
        @PathVariable String id
    ) {
        try {
            List<Episode> episodes = showService.findEpisodesBySeasons(page, size, UUID.fromString(id));
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", showService.getEpisodesBySeasonsPageCount(size, UUID.fromString(id)).toString());

            return new ResponseEntity<>(episodes, headers, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }
    

    @PostMapping("/{id}/episodes")
    public ResponseEntity<Episode> addEpisode(@PathVariable String id, @RequestBody JsonNode jsonNode) {
        try {
            Episode episode = Episode.builder()
                .title(jsonService.getOrNull(jsonNode, "title", String.class))
                .number(jsonService.getOrNull(jsonNode, "number", String.class))
                .description(jsonService.getOrNull(jsonNode, "description", String.class))
                .url(jsonService.getOrNull(jsonNode, "url", String.class))
                .releaseDate(jsonNode.has("releaseDate")?LocalDate.parse(jsonNode.get("releaseDate").asText()):null)
                .duration(jsonService.getOrNull(jsonNode, "duration", Integer.class))
                .openingStart(jsonService.getOrNull(jsonNode, "openingStart", Integer.class))
                .openingEnd(jsonService.getOrNull(jsonNode, "openingEnd", Integer.class))
                .view(0)
                .season(UUID.fromString(id))
                .build();
            
            Episode returnEpisode = showService.addEpisode(episode);

            return new ResponseEntity<>(returnEpisode, HttpStatus.CREATED);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    
    
}
