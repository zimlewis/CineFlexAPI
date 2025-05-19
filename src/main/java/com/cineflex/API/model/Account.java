package com.cineflex.API.model;

import java.time.LocalDateTime;

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
