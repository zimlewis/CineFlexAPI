package com.cineflex.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Like;


@Repository
public class LikeRepository implements RepositoryInterface<Like>{
    private final JdbcClient jdbcClient;

    public LikeRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(Like t) {
        String sql = "INSERT INTO [dbo].[Like] ([Account], [Episode], [CreatedTime]) VALUES (?, ?, ?)";

        int row = jdbcClient
            .sql(sql)
            .params(t.getAccount(), t.getEpisode(), t.getCreatedTime())
            .update();

        if (row == 0) {
            throw new RuntimeException("Cannot like this episode");
        }
    }

    @Override
    public Like read(UUID id) {
        return null;
    }

    @Override
    public List<Like> readAll() {
        String sql = "SELECT * FROM [dbo].[Like]";
        
        List<Like> likes = jdbcClient
            .sql(sql)
            .query(Like.class)
            .list();
        
        return likes;
    }

    @Override
    public void update(UUID id, Like t) {
    }

    @Override
    public void delete(UUID... ids) {
    }
    

}
