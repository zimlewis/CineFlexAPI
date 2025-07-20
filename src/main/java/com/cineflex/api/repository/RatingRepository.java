package com.cineflex.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Rating;

@Repository
public class RatingRepository implements RepositoryInterface<Rating>{
    private final JdbcClient jdbcClient;

    public RatingRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public void create(Rating t) {
        String sql = "INSERT INTO [dbo].[Rating] ([Account], [Show], [Value], [CreatedTime]) VALUES (?, ?, ?, ?)";
        
        jdbcClient.sql(sql).params(
            t.getAccount(),
            t.getShow(),
            t.getValue(),
            t.getCreatedTime()
        ).update();
    }

    @Override
    public Rating read(UUID id) {
        return null;
    }

    @Override
    public List<Rating> readAll(Integer page, Integer size) {
        String sql = "SELECT * FROM [dbo].[Rating] ORDER BY [CreatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<Rating> ratings = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(Rating.class)
            .list();
        
        return ratings;
    }

    @Override
    public void update(UUID id, Rating t) {
    }

    @Override
    public void delete(UUID... ids) {
    }


    @Override
    public List<Rating> readAll() {
        return readAll(0, 5);
    }


    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Rating]";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
    }
    
}
