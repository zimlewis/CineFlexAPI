package com.cineflex.API.model;

import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Builder
@Data
public class ShowGenre {
    UUID show;
    UUID genre;
}
