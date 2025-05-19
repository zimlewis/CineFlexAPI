package com.cineflex.API.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cineflex.API.model.Account;

@Repository
public class AccountRepository {
    private JdbcTemplate jdbcTemplate;

    public AccountRepository(
        JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Account account) {
        String sql = "INSERT INTO [dbo].[Account] ";

        jdbcTemplate.update(sql);
    }

}