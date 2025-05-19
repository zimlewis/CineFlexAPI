package com.cineflex.API.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Rating {
    Long account;
    Long show;
    Integer value;
    LocalDateTime createdDate;
    
}
