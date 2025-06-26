package com.cineflex.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Season {
    UUID id;
    String title;
    LocalDate releaseDate;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    String description;
    UUID show;
    Boolean isDeleted;
}
