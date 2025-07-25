package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Genre;
import com.cineflex.api.service.GenreService;
import com.cineflex.api.service.JsonService;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
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
@RequestMapping("/api/genres")
public class GenreAPI {
    private final JsonService jsonService;
    private final GenreService genreService;

    public GenreAPI (
        JsonService jsonService,
        GenreService genreService
    ) {
        this.jsonService = jsonService;
        this.genreService = genreService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable String id) {
        try {
            Genre genre = genreService.getGenre(UUID.fromString(id));
            
            return new ResponseEntity<Genre>(genre, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("search")
    public ResponseEntity<List<Genre>> search (
        @RequestParam(required = false) String keyword
    ) {
        try {
            List<Genre> genres = genreService.searchGenres(keyword);

            return new ResponseEntity<>(genres, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Genre>> getAllGenres() {
        try {
            List<Genre> genres = genreService.getAllGenres();

            return new ResponseEntity<>(genres, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    
    
    
    
    @PostMapping("")
    public ResponseEntity<Genre> addGenre(@RequestBody JsonNode jsonNode) {
        try {
            String name = jsonService.getOrNull(jsonNode, "name", String.class);

            if (name == null) {
                throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Incorect field"
                );
            }

            Genre genre = Genre.builder()
                .name(name).build();
            
            Genre returnedGenre = genreService.addGenre(genre);

            return new ResponseEntity<Genre>(returnedGenre, HttpStatus.CREATED);
            
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    

}
