package com.cineflex.API.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.API.model.Account;

@Repository
public class AccountRepository implements RepositoryInterface<Account>{
    private final JdbcClient jdbcClient;

    public AccountRepository(
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    public void create(Account account) {
        String sql = "INSERT INTO [dbo].[Account] ([Id], [Username], [Email], [Password], [CreatedTime], [UpdatedTime], [Verify], [Role]) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int row = jdbcClient.sql(sql).params(
            account.getId(), 
            account.getUsername(), 
            account.getEmail(), 
            account.getPassword(), 
            account.getCreatedTime(), 
            account.getUpdatedTime(), 
            account.getVerify(), 
            account.getRole()
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot add account to database");
        }
    }

    public Account read(UUID id) {
        Account account = null;
        String sql = "SELECT * FROM [dbo].[Account] WHERE [Id] = ?";

        account = jdbcClient.sql(sql)
            .params(id)
            .query(Account.class)
            .optional()
            .orElse(null);

        return account;
    }

    public List<Account> readAll() {
        List<Account> accounts = new ArrayList<Account>();

        String sql = "SELECT * FROM [dbo].[Account]";

        accounts = jdbcClient
            .sql(sql)
            .query(Account.class)
            .list();

        return accounts;
    }

    public void update(UUID id, Account account) {
        String sql = "UPDATE [dbo].[Account] SET [Username] = ?, [Email] = ?, [Password] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [Verify] = ?, [Role] = ? WHERE [Id] = ?";
        
        int row = jdbcClient.sql(sql).params(
            account.getUsername(), 
            account.getEmail(), 
            account.getPassword(), 
            account.getCreatedTime(), 
            account.getUpdatedTime(), 
            account.getVerify(), 
            account.getRole(), 
            id
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot update account");
        }
    }

    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));
        
        String sql = "DELETE FROM [dbo].[Account] WHERE [Id] IN (" + placeholders + ")";

        int row = jdbcClient.sql(sql).params(Arrays.asList(ids)).update();

        if (row == 0) {
            throw new RuntimeException("Cannot delete account");
        }
    }
}