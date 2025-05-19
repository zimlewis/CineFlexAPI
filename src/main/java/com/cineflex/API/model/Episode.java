package com.cineflex.API.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Episode {
    Long id;
    String title;
    Integer number;
    String description;
    String url;
    LocalDate releaseDate;
    LocalDateTime uploadDate;
    Integer duration;
    
}
