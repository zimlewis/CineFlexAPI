package com.cineflex.API.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Account {
    Long id;
    String username;
    String email;
    String password;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    Boolean verify;
    Integer role;
}
