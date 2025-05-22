package com.cineflex.API.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Favorite {
    UUID account;
    UUID show;
    LocalDateTime createdTime;
}
