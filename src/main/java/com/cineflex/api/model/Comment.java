package com.cineflex.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Comment {
    UUID id;
    String content;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    UUID account;
    UUID episode;
}
