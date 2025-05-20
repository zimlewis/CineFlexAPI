package com.cineflex.API.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class Like {
    UUID account;
    UUID episode;
    LocalDateTime createdDate;
}
