package com.cineflex.api.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Account;
import com.cineflex.api.model.Show;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.FavoriteService;

import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteAPI {

    private final FavoriteService favoriteService;
    private final AuthenticationService authenticationService;

    public FavoriteAPI(FavoriteService favoriteService, AuthenticationService authenticationService) {
        this.favoriteService = favoriteService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/{showId}")
    public ResponseEntity<?> addFavorite(@PathVariable String showId) {
        try {
            Account account = authenticationService.getAccount();
            favoriteService.addFavorite(account.getId(), UUID.fromString(showId));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.of(
                    ProblemDetail.forStatusAndDetail(e.getStatusCode(), e.getReason())
            ).build();
        }
    }

    @DeleteMapping("/{showId}")
    public ResponseEntity<?> removeFavorite(@PathVariable String showId) {
        try {
            Account account = authenticationService.getAccount();
            favoriteService.removeFavorite(account.getId(), UUID.fromString(showId));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return ResponseEntity.of(
                    ProblemDetail.forStatusAndDetail(e.getStatusCode(), e.getReason())
            ).build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Show>> getFavorites(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "6") Integer size) {
        try {
            Account account = authenticationService.getAccount();
            List<Show> favorites = favoriteService.getFavoriteShows(account.getId(), page, size);

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", favoriteService.getFavoritesPageCount(account.getId(), size).toString());

            return new ResponseEntity<>(favorites, headers, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return ResponseEntity.of(
                    ProblemDetail.forStatusAndDetail(e.getStatusCode(), e.getReason())
            ).build();
        }
    }
}
