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
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[ViewHistory]";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
    }
    
}
