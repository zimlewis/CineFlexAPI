package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Hirer;
import com.cineflex.api.repository.HirerRepository;

@Service
public class AdvertisementService {
    private final HirerRepository hirerRepository;


    public AdvertisementService (
        HirerRepository hirerRepository
    ) {
        this.hirerRepository = hirerRepository;
    }

    public List<Hirer> getPaginatedHirer(Integer page, Integer size) {
        try {
            return hirerRepository.readAll(page, size);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Integer getHirerPage(Integer size) {
        try {
            return hirerRepository.getPageCount(size);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Hirer addHirer(Hirer hirer) {
        try {
            UUID id = UUID.randomUUID();

            hirer.setId(id);
            hirer.setCreatedTime(LocalDateTime.now());
            hirer.setUpdatedTime(LocalDateTime.now());

            hirerRepository.create(hirer);
            return hirerRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());   
        }
    }
}
