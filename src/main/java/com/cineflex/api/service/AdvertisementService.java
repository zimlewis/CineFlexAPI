package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Advertisement;
import com.cineflex.api.model.Hirer;
import com.cineflex.api.repository.AdvertisementRepository;
import com.cineflex.api.repository.HirerRepository;

@Service
public class AdvertisementService {
    private final HirerRepository hirerRepository;
    private final AdvertisementRepository advertisementRepository;


    public AdvertisementService (
        HirerRepository hirerRepository,
        AdvertisementRepository advertisementRepository
    ) {
        this.hirerRepository = hirerRepository;
        this.advertisementRepository = advertisementRepository;
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

    public List<Advertisement> getPaginatedAdvertisement(Integer page, Integer size) {
        try {
            return advertisementRepository.readAll(page, size);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }   
    }

    public Advertisement getRandomTypeAdvertisement(Integer type) {
        try {
            return advertisementRepository.findRandomOnType(type);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Integer getAdvertisementPageCount(Integer size) {
        try {
            return advertisementRepository.getPageCount(size);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }   
    }

    public Advertisement addAdvertisement(Advertisement advertisement) {
        try {
            UUID id = UUID.randomUUID();

            advertisement.setId(id);
            advertisement.setCreatedTime(LocalDateTime.now());
            advertisement.setUpdatedTime(LocalDateTime.now());

            advertisementRepository.create(advertisement);
            return advertisementRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Hirer getHirer(UUID id) {
        try {
            Hirer hirer = hirerRepository.read(id);
            return hirer;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
