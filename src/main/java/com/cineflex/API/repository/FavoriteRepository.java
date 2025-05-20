package com.cineflex.API.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.API.model.Favorite;

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
        String sql = "INSERT INTO [dbo].[Favorite]([Account], [Show]) VALUES (?, ?)";
        
        int row = jdbcClient.sql(sql).params(
            t.getAccount(), 
            t.getShow(), 
            t.getCreatedDate()
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot add favorite this");
        }
    }

    @Override
    public Favorite read(UUID id) {
        return null;
    }

    @Override
    public List<Favorite> readAll() {
        String sql = "SELECT * FROM [dbo].[Favorite]";
        List<Favorite> favorites = jdbcClient.sql(sql).query(Favorite.class).list();

        return favorites;
    }

    @Override
    public void update(UUID id, Favorite t) {
    }

    @Override
    public void delete(UUID id) {

    }
    
}
