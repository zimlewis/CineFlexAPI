package com.cineflex.API.model;

import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Genre {
    UUID id;
    String name;
}
