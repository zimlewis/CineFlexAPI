package com.cineflex.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.Builder;

@Builder
@Data
public class ReportComment {
    UUID id;
    String content;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    Integer status;
    UUID account;
    UUID comment;
}
