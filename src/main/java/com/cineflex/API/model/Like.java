package com.cineflex.API.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Like {
    Long account;
    Long episode;
    LocalDateTime createdDate;
}
