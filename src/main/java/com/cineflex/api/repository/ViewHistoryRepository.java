package com.cineflex.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.ViewHistory;

@Repository
public class ViewHistoryRepository implements RepositoryInterface<ViewHistory>{
    private final JdbcClient jdbcClient;

    public ViewHistoryRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public void create(ViewHistory t) {
        String sql = "INSERT INTO [dbo].[ViewHistory] ([Account], [Episode], [CreatedTime], [UpdatedTime], [Duration]) VALUES (?, ?, ?, ?, ?)";
        
        jdbcClient.sql(sql)
            .params(
                t.getAccount(), 
                t.getEpisode(),
                t.getCreatedTime(),
                t.getUpdatedTime(),
                t.getDuration()
            )
            .update();
        
    }

    @Override
    public ViewHistory read(UUID id) {
        return null;
    }

    @Override
    public List<ViewHistory> readAll(Integer page, Integer size) {
        String sql = "SELECT * FROM [dbo].[ViewHistory] WHERE [IsDeleted] = 0 ORDER BY [CreatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<ViewHistory> viewHistories = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(ViewHistory.class)
            .list();
        
        return viewHistories;
    }

    @Override
    public void update(UUID id, ViewHistory t) {
    }

    @Override
    public void delete(UUID... ids) {
    }


    @Override
    public List<ViewHistory> readAll() {
        return readAll(0, 5);
    }


    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT(*)/? FROM [dbo].[ViewHistory]";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
    }

    public ViewHistory getViewHistory(UUID account, UUID episode) {
        String sql = "SELECT * FROM [dbo].[ViewHistory] WHERE [Account] =? AND [Episode] = ?";

        ViewHistory viewHistory = jdbcClient.sql(sql).params(account, episode).query(ViewHistory.class).optional().orElse(null);

        return viewHistory;
    }

    public void updateViewHisotry(ViewHistory viewHistory) {
        String sql = "UPDATE [dbo].[ViewHistory] SET [CreatedTime] = ?, [UpdatedTime] = ?, [Duration] = ? WHERE [Account] = ? AND [Episode] = ?";
        
        jdbcClient.sql(sql)
            .params(
                viewHistory.getCreatedTime(),
                viewHistory.getUpdatedTime(),
                viewHistory.getDuration(),
                viewHistory.getAccount(), 
                viewHistory.getEpisode()
            )
            .update();
    }
    

    public List<ViewHistory> getViewHistoriesByAccount(Integer page, Integer size, UUID account) {
        String sql = "SELECT * FROM [dbo].[ViewHistory] WHERE [IsDeleted] = 0 AND [Account] = ? ORDER BY [UpdatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<ViewHistory> viewHistories = jdbcClient
            .sql(sql)
            .params(account, page * size, size)
            .query(ViewHistory.class)
            .list();
        
        return viewHistories;
    }

    public Integer getViewHistoriesByAccountPageCount(Integer size, UUID account) {
        String sql = "SELECT COUNT(*)/? FROM [dbo].[ViewHistory] WHERE [IsDeleted] = 0 AND [Account] = ?";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size, account)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
    }
}
