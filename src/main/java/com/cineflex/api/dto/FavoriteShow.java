package com.cineflex.api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FavoriteShow {
    private UUID account;
    private UUID showId;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private String thumbnail;
    private Boolean onGoing;
    private Boolean isSeries;
    private String ageRating;
    private LocalDateTime createdTime;
}
