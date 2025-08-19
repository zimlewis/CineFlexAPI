package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.dto.FavoriteShow;
import com.cineflex.api.model.Favorite;
import com.cineflex.api.repository.FavoriteRepository;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public void addFavorite(UUID account, UUID show) {
        Favorite existing = favoriteRepository.getFavorite(account, show);
        if (existing != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already favorited this show");
        }

        Favorite f = Favorite.builder()
                .account(account)
                .show(show)
                .createdTime(LocalDateTime.now())
                .build();

        favoriteRepository.create(f);
    }

    public void removeFavorite(UUID account, UUID show) {
        Favorite existing = favoriteRepository.getFavorite(account, show);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite not found");
        }
        favoriteRepository.removeFavorite(account, show);
    }

    public List<FavoriteShow> getFavorites(UUID account, int page, int size) {
        return favoriteRepository.getFavoritesByAccount(account, page, size);
    }

    public Integer getFavoritesPageCount(UUID account, int size) {
        return favoriteRepository.getFavoritesPageCount(account, size);
    }

    public Integer getFavoriteCount(UUID show) {
        return favoriteRepository.getFavoriteCount(show);
    }
}
