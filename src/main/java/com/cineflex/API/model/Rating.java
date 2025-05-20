package com.cineflex.API.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class Rating {
    UUID account;
    UUID show;
    Integer value;
    LocalDateTime createdDate;
    
}
