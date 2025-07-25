package com.cineflex.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Genre;
import com.cineflex.api.repository.GenreRepository;

@Service
public class GenreService {
    
    private final GenreRepository genreRepository;

    public GenreService (
        GenreRepository genreRepository
    ) {
        this.genreRepository = genreRepository;
    }


    public Genre addGenre(Genre genre) {
        try {
            UUID id = UUID.randomUUID();
            genre.setId(id);

            genreRepository.create(genre);

            Genre returnedGenre = genreRepository.read(id);

            return returnedGenre;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Genre getGenre(UUID id) {
        try {
            Genre genre = genreRepository.read(id);

            return genre;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    
    public List<Genre> getAllGenres() {
        try {
            List<Genre> genres = genreRepository.readAll(0, 100);

            return genres;

        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Genre> searchGenres(String keyword) {
        try {
            List<Genre> genres = genreRepository.searchGenres(keyword);

            return genres;

        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
