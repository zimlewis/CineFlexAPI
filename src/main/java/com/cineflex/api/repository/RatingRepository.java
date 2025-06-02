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
        
        int row = jdbcClient.sql(sql).params(
            t.getAccount(),
            t.getShow(),
            t.getValue(),
            t.getCreatedTime()
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot rate this at the moment");
        }
    }

    @Override
    public Rating read(UUID id) {
        return null;
    }

    @Override
    public List<Rating> readAll() {
        String sql = "SELECT * FROM [dbo].[Rating]";

        List<Rating> ratings = jdbcClient
            .sql(sql)
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
    
}
