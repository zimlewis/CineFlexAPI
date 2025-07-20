package com.cineflex.api.model;

import java.util.UUID;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Genre {
    UUID id;
    String name;
    Boolean isDeleted;
}
