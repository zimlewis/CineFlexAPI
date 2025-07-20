package com.cineflex.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Favorite;

@Repository
public class FavoriteRepository implements RepositoryInterface<Favorite>{

    private final JdbcClient jdbcClient;

    public FavoriteRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(Favorite t) {
        String sql = "INSERT INTO [dbo].[Favorite] ([Account], [Show], [CreatedTime]) VALUES (?, ?, ?)";
        
        jdbcClient.sql(sql).params(
            t.getAccount(), 
            t.getShow(), 
            t.getCreatedTime()
        ).update();
    }

    @Override
    public Favorite read(UUID id) {
        return null;
    }

    @Override
    public List<Favorite> readAll(Integer page, Integer size) {
        String sql = "SELECT * FROM [dbo].[Favorite] ORDER BY [CreatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Favorite> favorites = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(Favorite.class)
            .list();

        return favorites;
    }

    @Override
    public void update(UUID id, Favorite t) {
    }

    @Override
    public void delete(UUID... ids) {

    }

    @Override
    public List<Favorite> readAll() {
        return readAll(0, 5);
    }

    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Favorite]";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
    }
    
}
