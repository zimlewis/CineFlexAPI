package com.cineflex.API.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Season {
    Long id;
    String title;
    LocalDate releaseDate;
    String description;
    Long show;
}
