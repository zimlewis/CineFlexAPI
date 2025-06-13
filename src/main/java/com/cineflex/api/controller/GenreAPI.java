package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Genre;
import com.cineflex.api.service.GenreService;
import com.cineflex.api.service.JsonService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/genre")
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
                e.getMessage()
            )).build();
        }
    }
    

}
