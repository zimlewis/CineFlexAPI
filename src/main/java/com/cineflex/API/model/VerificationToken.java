package com.cineflex.API.model;

import java.util.UUID;

import lombok.Data;

@Data
public class VerificationToken {
    UUID id;
    UUID account;
    String token;
}
