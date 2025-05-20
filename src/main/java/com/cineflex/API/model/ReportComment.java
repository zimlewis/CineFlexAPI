package com.cineflex.API.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ReportComment {
    UUID id;
    String content;
    LocalDateTime reportedTime;
    Integer status;
    UUID account;
    UUID comment;
}
