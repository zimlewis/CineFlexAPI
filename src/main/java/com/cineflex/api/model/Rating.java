package com.cineflex.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Builder
@Data
public class Rating {
    UUID account;
    UUID show;
    Integer value;
    LocalDateTime createdTime;
    
}
