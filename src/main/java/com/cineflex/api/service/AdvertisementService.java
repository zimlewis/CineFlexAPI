package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    public Advertisement updateAdvertisement(Advertisement advertisement) {
        try {
            UUID id = advertisement.getId();
            Advertisement oldAdvertisement = advertisementRepository.read(id);

            advertisement.setLink(Objects.requireNonNullElse(advertisement.getLink(), oldAdvertisement.getLink()));
            advertisement.setImage(Objects.requireNonNullElse(advertisement.getImage(), oldAdvertisement.getImage()));
            advertisement.setEnabled(Objects.requireNonNullElse(advertisement.getEnabled(), oldAdvertisement.getEnabled()));
            advertisement.setType(Objects.requireNonNullElse(advertisement.getType(), oldAdvertisement.getType()));
            advertisement.setCreatedTime(oldAdvertisement.getCreatedTime());
            advertisement.setUpdatedTime(LocalDateTime.now());
            advertisement.setHirer(oldAdvertisement.getHirer());

            advertisementRepository.update(id, advertisement);

            return advertisementRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } 
    }

    public Hirer updateHirer(Hirer hirer) {
        try {
            UUID id = hirer.getId();
            Hirer oldHirer = hirerRepository.read(id);

            hirer.setAlias(Objects.requireNonNullElse(hirer.getAlias(), oldHirer.getAlias()));
            hirer.setEmail(Objects.requireNonNullElse(hirer.getEmail(), oldHirer.getEmail()));
            hirer.setPhone(Objects.requireNonNullElse(hirer.getPhone(), oldHirer.getPhone()));
            hirer.setCreatedTime(oldHirer.getCreatedTime());
            hirer.setUpdatedTime(LocalDateTime.now());


            hirerRepository.update(id, hirer);

            hirer = hirerRepository.read(id);

            return hirer;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } 
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

    public Integer getTotalAdvertisementCount() {
        try {
            return advertisementRepository.getTotalCount();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
