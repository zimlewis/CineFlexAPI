package com.cineflex.API.model;

import lombok.Data;

@Data
public class VerifyToken {
    Long id;
    Long account;
    String token;
}
