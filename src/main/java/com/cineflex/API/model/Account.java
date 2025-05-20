package com.cineflex.API.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class Account {
    UUID id;
    String username;
    String email;
    String password;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    Boolean verify;
    Integer role;
}
