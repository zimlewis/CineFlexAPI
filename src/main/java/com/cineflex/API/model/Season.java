package com.cineflex.API.model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class Season {
    UUID id;
    String title;
    LocalDate releaseDate;
    String description;
    UUID show;
}
