package com.cineflex.API.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class Episode {
    UUID id;
    String title;
    Integer number;
    String description;
    String url;
    LocalDate releaseDate;
    LocalDateTime uploadedTime;
    Integer duration;
    
}
