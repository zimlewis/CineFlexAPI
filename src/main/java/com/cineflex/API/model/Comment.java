package com.cineflex.API.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class Comment {
    UUID id;
    String content;
    LocalDateTime commentedTime;
    UUID account;
    UUID episode;
}
