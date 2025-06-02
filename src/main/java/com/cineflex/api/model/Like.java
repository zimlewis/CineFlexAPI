package com.cineflex.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Like {
    UUID account;
    UUID episode;
    LocalDateTime createdTime;
}
