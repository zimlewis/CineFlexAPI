package com.cineflex.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.BillingDetail;

@Repository
public class BillingDetailRepository implements RepositoryInterface<BillingDetail>{
    private JdbcClient jdbcClient;

    public BillingDetailRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(BillingDetail t) {
        String sql = "INSERT INTO [dbo].[BillingDetail] ([Id], [Account], [Subscription], [Amount], [CreatedTime], [PaidTime], [Paid], [TransactionCode]) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcClient.sql(sql).params(
            t.getId(),
            t.getAccount(),
            t.getSubscription(),
            t.getAmount(),
            t.getCreatedTime(),
            t.getPaidTime(),
            t.getPaid(),
            t.getTransactionCode()
        ).update();

        
    }

    @Override
    public BillingDetail read(UUID id) {
        String sql = "SELECT * FROM [dbo].[BillingDetail] WHERE [Id] = ?";

        BillingDetail b = jdbcClient
            .sql(sql)
            .params(id)
            .query(BillingDetail.class)
            .optional()
            .orElse(null);

        return b;
    }

    @Override
    public List<BillingDetail> readAll(Integer page, Integer size) {
        String sql = "SELECT * FROM [dbo].[BillingDetail] ORDER BY [CreatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<BillingDetail> billingDetails = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(BillingDetail.class).list();

        return billingDetails;
    }

    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[BillingDetail]";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
    }

    @Override
    public void update(UUID id, BillingDetail t) {
        String sql = "UPDATE [dbo].[BillingDetail] SET [Account] = ?, [Subscription] = ?, [Amount] = ?, [CreatedTime] = ?, [PaidTime] = ?, [Paid] = ?, [TransactionCode] = ? WHERE [Id] = ?";
    
        jdbcClient.sql(sql).params(
            t.getAccount(),
            t.getSubscription(),
            t.getAmount(),
            t.getCreatedTime(),
            t.getPaidTime(),
            t.getPaid(),
            t.getTransactionCode(),
            id
        ).update();
    }

    @Override
    public void delete(UUID... ids) {
    }

    public BillingDetail getAccountUnpaid(UUID account) {
        String sql = "SELECT * FROM [dbo].[BillingDetail] WHERE [Account] = ? AND [Paid] = 0";

        BillingDetail b = jdbcClient
            .sql(sql)
            .params(account)
            .query(BillingDetail.class)
            .optional()
            .orElse(null);

        return b;
    }

    public BillingDetail getByTransactionCode(String code) {
        String sql = "SELECT * FROM [dbo].[BillingDetail] WHERE [TransactionCode] = ?";

        BillingDetail b = jdbcClient
            .sql(sql)
            .params(code)
            .query(BillingDetail.class)
            .optional()
            .orElse(null);

        return b;
    }

    @Override
    public List<BillingDetail> readAll() {
        return readAll(0, 5);
    }

    public Double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM([Amount]), 0) FROM [dbo].[BillingDetail] WHERE [Paid] = 1";

        return jdbcClient
                .sql(sql)
                .query(Double.class)
                .optional()
                .orElse(0.0);
    }

}
