package com.cineflex.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillingDetail {
    UUID id;
    UUID account;
    UUID subscription;
    Double amount;
    LocalDateTime createdTime;
    LocalDateTime paidTime;
    Boolean paid;
    String transactionCode;
}
