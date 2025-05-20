package com.cineflex.API.model;

import java.util.UUID;

import lombok.Data;

@Data
public class Comment {
    UUID id;
    String content;
    UUID account;
    UUID episode;
}
