package com.cineflex.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewHistory {
    UUID account;
    UUID episode;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    Integer duration;
}
