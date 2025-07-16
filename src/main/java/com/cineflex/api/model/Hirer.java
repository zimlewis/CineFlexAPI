package com.cineflex.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Hirer {
    UUID id;
    String alias;
    String email;
    String phone;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
}
