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

        jdbcClient
            .sql(sql)
            .params(t.getAccount(), t.getEpisode(), t.getCreatedTime())
            .update();
    }

    @Override
    public Like read(UUID id) {
        return null;
    }

    @Override
    public List<Like> readAll(Integer page, Integer size) {
        String sql = "SELECT * FROM [dbo].[Like] LIMIT ? OFFSET ?";
        
        List<Like> likes = jdbcClient
            .sql(sql)
            .params(size, page * size)
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

    @Override
    public List<Like> readAll() {
        return readAll(0, 5);
    }
    

}
