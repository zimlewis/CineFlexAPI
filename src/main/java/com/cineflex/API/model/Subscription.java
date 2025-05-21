package com.cineflex.API.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Subscription {
    UUID id;
    LocalDateTime startTime;
    LocalDateTime endTime;
    UUID account;
}
