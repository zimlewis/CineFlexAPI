package com.cineflex.API.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Favorite {
    Long account;
    Long show;
    LocalDateTime createdDate;
}
