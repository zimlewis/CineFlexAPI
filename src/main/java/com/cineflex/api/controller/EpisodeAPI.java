package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Comment;
import com.cineflex.api.model.Episode;
import com.cineflex.api.service.CommentService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/episodes")
public class EpisodeAPI {

    private final ShowService showService;
    private final JsonService jsonService;
    private final CommentService commentService;

    public EpisodeAPI (
        ShowService showService,
        JsonService jsonService,
        CommentService commentService
    ) {
        this.showService = showService;
        this.jsonService = jsonService;
        this.commentService = commentService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Episode> getEpisodeById(@PathVariable String id) {
        try {
            Episode episode = showService.findEpisodeById(UUID.fromString(id));

            if (episode == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND, 
                    "Cannot find the season with given id"
                )).build();
            }

            return new ResponseEntity<>(episode, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Episode> updateEpisode(@PathVariable String id, @RequestBody JsonNode jsonNode) {
        try {
            UUID seasonId = jsonService.getOrNull(jsonNode, "season", String.class) == null?null : UUID.fromString(jsonService.getOrNull(jsonNode, "show", String.class));
            Episode episode = Episode.builder()
                .id(UUID.fromString(id))
                .title(jsonService.getOrNull(jsonNode, "title", String.class))
                .number(jsonService.getOrNull(jsonNode, "number", String.class))
                .description(jsonService.getOrNull(jsonNode, "description", String.class))
                .url(jsonService.getOrNull(jsonNode, "url", String.class))
                .releaseDate(jsonNode.has("releaseDate")?LocalDate.parse(jsonNode.get("releaseDate").asText()):null)
                .duration(jsonService.getOrNull(jsonNode, "duration", Integer.class))
                .openingStart(jsonService.getOrNull(jsonNode, "openingStart", Integer.class))
                .openingEnd(jsonService.getOrNull(jsonNode, "openingEnd", Integer.class))
                .view(0)
                .season(seasonId)
                .build();
            
            Episode returnEpisode = showService.updateEpisode(episode);

            return new ResponseEntity<>(returnEpisode, HttpStatus.OK);
            
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEpisode(@PathVariable String id) {
        try {
            showService.deleteEpisode(UUID.fromString(id));

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String id) {
        try {
            List<Comment> comments = commentService.getAllCommentsFromEpisode(0, 5, UUID.fromString(id));

            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }
    
    
}
