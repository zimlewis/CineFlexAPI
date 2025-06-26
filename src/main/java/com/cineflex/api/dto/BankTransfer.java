package com.cineflex.api.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class BankTransfer {
    String gateway;
    String transactionDate;
    String accountNumber;
    String subAccount;
    String code;
    String content;
    String transferType;
    String description;
    Double transferAmount;
    UUID referenceCode;
    Float accumulated;
    Float id;
}
