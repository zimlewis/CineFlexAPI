package com.cineflex.API.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Show {
    Long id;
    String title;
    String description;
    LocalDate releaseDate;
    String thumbnail;
    LocalDateTime createDate;
    Boolean onGoing;
    Boolean isSeries;
}
