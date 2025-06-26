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
        
        int row = jdbcClient.sql(sql)
            .params(
                t.getAccount(), 
                t.getEpisode(),
                t.getCreatedTime(),
                t.getUpdatedTime(),
                t.getDuration()
            )
            .update();
        
        if (row == 0) {
            throw new RuntimeException("Cannot save view history");
        }
    }

    @Override
    public ViewHistory read(UUID id) {
        return null;
    }

    @Override
    public List<ViewHistory> readAll() {
        String sql = "SELECT * FROM [dbo].[ViewHistory] WHERE [IsDeleted] = 0";

        List<ViewHistory> viewHistories = jdbcClient.sql(sql).query(ViewHistory.class).list();
        return viewHistories;
    }

    @Override
    public void update(UUID id, ViewHistory t) {
    }

    @Override
    public void delete(UUID... ids) {
    }
    
}
