package com.cineflex.API.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Subscription {
    Long id;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Long account;
}
