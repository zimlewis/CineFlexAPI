package com.cineflex.api.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Flow.Subscription;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Account;
import com.cineflex.api.model.BillingDetail;

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
        String sql = "SELECT * FROM [dbo].[Account] WHERE [Id] = ?";

        account = jdbcClient.sql(sql)
            .params(id)
            .query(Account.class)
            .optional()
            .orElse(null);

        return account;
    }

    public List<Account> readAll(Integer page, Integer size) {
        List<Account> accounts = new ArrayList<Account>();

        String sql = "SELECT * FROM [dbo].[Account] ORDER BY [CreatedTime] OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        accounts = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(Account.class)
            .list();

        return accounts;
    }

    public void update(UUID id, Account account) {
        String sql = "UPDATE [dbo].[Account] SET [Username] = ?, [Email] = ?, [Password] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [Verify] = ?, [Role] = ?, [Activate] = ? WHERE [Id] = ?";
        
        jdbcClient.sql(sql).params(
            account.getUsername(), 
            account.getEmail(), 
            account.getPassword(), 
            account.getCreatedTime(), 
            account.getUpdatedTime(), 
            account.getVerify(), 
            account.getRole(), 
            account.getActivate(),
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

    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Account]";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(0);
        
        return pageCount;
    }

    public Integer getBillingDetailsOfAccountPageCount(Integer size, UUID account) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[BillingDetail] WHERE [Account] = ?";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size, account)
            .query(Integer.class).optional().orElse(0);
        
        return pageCount;
    }

    public List<BillingDetail> getBillingDetailsOfAccount(Integer page, Integer size, UUID account) {
        List<BillingDetail> billingDetails = new ArrayList<>();

        String sql = "SELECT * FROM [dbo].[BillingDetail] WHERE [Account] = ? ORDER BY [CreatedTime] OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        billingDetails = jdbcClient
            .sql(sql)
            .params(account, page * size, size)
            .query(BillingDetail.class)
            .list();

        return billingDetails;
    }

    public List<Account> getPremiumAccounts(Integer page, Integer size){
        List<Account> accounts = new ArrayList<Account>();

        String sql = "SELECT * FROM [dbo].[Account] WHERE [Id] IN (SELECT [Account] FROM [dbo].[Subscription] WHERE [EndTime] > GETDATE()) ORDER BY [Username] OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        accounts = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(Account.class)
            .list();

        return accounts;
    }

    public Integer getPremiumAccountsPageCount(Integer size){
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Account] WHERE [Id] IN (SELECT [Account] FROM [dbo].[Subscription] WHERE [EndTime] > GETDATE())";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(0);
        
        return pageCount;
    }
    public long countAllUsers() {
        String sql = "SELECT COUNT(*) FROM Account";
        return jdbcClient.sql(sql)
                .query(Long.class)
                .single();
    }

    public long countFreeUsers() {
        String sql = """
        
                SELECT COUNT(*) 
        FROM Account a 
        WHERE NOT EXISTS (
            SELECT 1 
            FROM Subscription s 
            WHERE s.account = a.id 
              AND s.endTime > GETDATE()
        )
        """;
        return jdbcClient.sql(sql)
                .query(Long.class)
                .single();
    }

    public long countActiveSubscriptions() {
        String sql = "SELECT COUNT(*) FROM Subscription WHERE endTime > GETDATE()";
        return jdbcClient.sql(sql)
                .query(Long.class)
                .single();
    }
}