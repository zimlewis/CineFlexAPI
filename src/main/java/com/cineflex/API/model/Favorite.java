package com.cineflex.API.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class Favorite {
    UUID account;
    UUID show;
    LocalDateTime createdDate;
}
