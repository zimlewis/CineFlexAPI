package com.cineflex.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Episode {
    UUID id;
    String title;
    String number;
    String description;
    String url;
    LocalDate releaseDate;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    Integer duration;
    Integer openingStart;
    Integer openingEnd;
    Integer view;
    UUID season;
    UUID commentSection;
    Boolean isDeleted;
}
