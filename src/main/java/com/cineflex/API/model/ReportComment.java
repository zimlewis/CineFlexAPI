package com.cineflex.API.model;

import lombok.Data;

@Data
public class ReportComment {
    Long id;
    String content;
    Long account;
    Long comment;
}
