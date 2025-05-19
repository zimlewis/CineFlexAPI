package com.cineflex.API.model;

import lombok.Data;

@Data
public class Comment {
    Long id;
    String content;
    Long account;
    Long episode;
}
