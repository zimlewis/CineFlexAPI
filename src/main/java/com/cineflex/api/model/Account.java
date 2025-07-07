package com.cineflex.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)

public class Account {
    UUID id;
    String username;
    String email;
    String password;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    Boolean verify;
    Integer role;
    Boolean activate;
}
