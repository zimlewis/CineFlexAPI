package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Favorite;
import com.cineflex.api.model.Show;
import com.cineflex.api.repository.FavoriteRepository;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public List<Show> getFavoriteShows(UUID account, int page, int size) {
        return favoriteRepository.getFavoriteShowsByAccount(account, page, size);
    }

    public Integer getFavoritesPageCount(UUID account, int size) {
        return favoriteRepository.getFavoritesPageCount(account, size);
    }

}
