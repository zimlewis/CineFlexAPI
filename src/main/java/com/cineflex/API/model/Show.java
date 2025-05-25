package com.cineflex.API.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Builder
@Data
public class Show {
    UUID id;
    String title;
    String description;
    LocalDate releaseDate;
    String thumbnail;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    Boolean onGoing;
    Boolean isSeries;
}
