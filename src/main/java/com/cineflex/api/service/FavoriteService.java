package com.cineflex.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Show;
import com.cineflex.api.repository.FavoriteRepository;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public List<Show> getFavoriteShows(UUID account, int page, int size) {
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account ID cannot be null");
        }
        if (page < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pagination parameters");
        }

        List<Show> shows = favoriteRepository.getFavoriteShowsByAccount(account, page, size);
        if (shows == null || shows.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No favorite shows found for this account");
        }
        return shows;
    }

    public Integer getFavoritesPageCount(UUID account, int size) {
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account ID cannot be null");
        }
        if (size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page size must be greater than zero");
        }

        Integer count = favoriteRepository.getFavoritesPageCount(account, size);
        if (count == null || count == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No favorite shows found for this account");
        }
        return count;
    }


    public Integer getFavoriteCount(UUID show) {
        if (show == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Show ID cannot be null");
        }

        Integer count = favoriteRepository.getFavoriteCount(show);
        if (count == null || count == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No favorites found for this show");
        }
        return count;
    }

    public List<Show> getMostFavoritedShows(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pagination parameters");
        }

        List<Show> shows = favoriteRepository.getMostFavoritedShows(page, size);
        if (shows == null || shows.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows found");
        }
        return shows;
    }

    public Integer getMostFavoritedShowsPageCount(int size) {
        if (size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page size must be greater than zero");
        }

        Integer count = favoriteRepository.getMostFavoritedShowsPageCount(size);
        if (count == null || count == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows found");
        }
        return count;
    }
}
