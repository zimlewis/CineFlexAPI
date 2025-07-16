package com.cineflex.api.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Account;

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

        jdbcClient.sql(sql).params(
            account.getId(), 
            account.getUsername(), 
            account.getEmail(), 
            account.getPassword(), 
            account.getCreatedTime(), 
            account.getUpdatedTime(), 
            account.getVerify(), 
            account.getRole()
        ).update();
    }

    public Account read(UUID id) {
        Account account = null;
        String sql = "SELECT * FROM [dbo].[Account] WHERE [Id] = ? AND [Activate] = 1";

        account = jdbcClient.sql(sql)
            .params(id)
            .query(Account.class)
            .optional()
            .orElse(null);

        return account;
    }

    public List<Account> readAll(Integer page, Integer size) {
        List<Account> accounts = new ArrayList<Account>();

        String sql = "SELECT * FROM [dbo].[Account] WHERE [Activate] = 1 LIMIT ? OFFSET ?";

        accounts = jdbcClient
            .sql(sql)
            .params(size, page * size)
            .query(Account.class)
            .list();

        return accounts;
    }

    public void update(UUID id, Account account) {
        String sql = "UPDATE [dbo].[Account] SET [Username] = ?, [Email] = ?, [Password] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [Verify] = ?, [Role] = ? WHERE [Id] = ? AND [Activate] = 1";
        
        jdbcClient.sql(sql).params(
            account.getUsername(), 
            account.getEmail(), 
            account.getPassword(), 
            account.getCreatedTime(), 
            account.getUpdatedTime(), 
            account.getVerify(), 
            account.getRole(), 
            id
        ).update();
    }

    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));
        
        String sql = "UPDATE [dbo].[Account] SET [Activate] = 0 WHERE [Id] IN (" + placeholders + ")";

        jdbcClient.sql(sql).params(Arrays.asList(ids)).update();
    }

    public Account readByUsername(String username) {
        Account account = null;
        String sql = "SELECT * FROM [dbo].[Account] WHERE [Username] = ? AND [Activate] = 1";

        account = jdbcClient.sql(sql)
            .params(username)
            .query(Account.class)
            .optional()
            .orElse(null);

        return account;
    }

    public Account readByEmail(String email) {
        Account account = null;
        String sql = "SELECT * FROM [dbo].[Account] WHERE [Email] = ?";

        account = jdbcClient.sql(sql)
            .params(email)
            .query(Account.class)
            .optional()
            .orElse(null);

        return account;
    }

    @Override
    public List<Account> readAll() {
        return readAll(0, 10);
    }
}