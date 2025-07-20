package com.cineflex.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.Builder;


@Data
@Builder
public class Advertisement {
    UUID id;
    String link;
    String image;
    Boolean enabled;
    Integer type;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    UUID hirer;
}
