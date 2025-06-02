package com.cineflex.api.model;

import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class VerificationToken {
    UUID id;
    UUID account;
    String token;
}
