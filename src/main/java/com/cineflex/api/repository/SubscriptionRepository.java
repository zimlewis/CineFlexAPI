package com.cineflex.api.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Subscription;

@Repository
public class SubscriptionRepository implements RepositoryInterface<Subscription> {

    private final JdbcClient jdbcClient;

    public SubscriptionRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(Subscription t) {
        String sql = "INSERT INTO [dbo].[Subscription] ([Id], [StartTime], [EndTime], [Account]) VALUES (?, ?, ?, ?)";

        jdbcClient.sql(sql)
            .params(
                t.getId(),
                t.getStartTime(),
                t.getEndTime(),
                t.getAccount()
            ).update();

    }

    @Override
    public Subscription read(UUID id) {
        String sql = "SELECT * FROM [dbo].[Subscription] WHERE [Id] = ?";

        Subscription subscription = jdbcClient
            .sql(sql)
            .params(id)
            .query(Subscription.class)
            .optional()
            .orElse(null);
        
        return subscription;
    }

    @Override
    public List<Subscription> readAll(Integer page, Integer size) {
        String sql = "SELECT * FROM [dbo].[Subscription] ORDER BY [StartTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<Subscription> subscriptions = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(Subscription.class)
            .list();
        
        return subscriptions;
    }

    @Override
    public void update(UUID id, Subscription t) {
        String sql = "UPDATE [dbo].[Subscription] SET [StartTime] = ?, [EndTime] = ?, [Account] = ? WHERE [Id] = ?";

        jdbcClient.sql(sql)
            .params(
                t.getStartTime(),
                t.getEndTime(),
                t.getAccount(),
                id
            ).update();
    }

    @Override
    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "DELETE FROM [dbo].[Subscription] WHERE [Id] IN (" + placeholders + ")";

        jdbcClient.sql(sql).params(Arrays.asList(ids)).update();
    }
    
    public Subscription getActivatedByAccount(UUID account) {
        String sql = "SELECT * FROM [dbo].[Subscription] WHERE [Account] = ? AND [EndTime] > GETDATE()";

        Subscription subscription = jdbcClient.sql(sql).params(account).query(Subscription.class).optional().orElse(null);

        return subscription;
    }

    @Override
    public List<Subscription> readAll() {
        return readAll(0, 5);
    }

    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Subscription]";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
    } 

}
