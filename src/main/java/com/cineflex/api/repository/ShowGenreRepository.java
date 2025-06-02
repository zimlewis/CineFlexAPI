package com.cineflex.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.ShowGenre;

@Repository
public class ShowGenreRepository implements RepositoryInterface<ShowGenre> {
    private final JdbcClient jdbcClient;

    public ShowGenreRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public void create(ShowGenre t) {
        String sql = "INSERT INTO [dbo].[ShowGenre] ([Show], [Genre]) VALUES (?, ?)";

        int row = jdbcClient
            .sql(sql)
            .params(t.getShow(), t.getGenre())
            .update();
        
        if (row == 0) {
            throw new RuntimeException("Cannot add this genre to show");
        } 
    }

    @Override
    public ShowGenre read(UUID id) {
        return null;
    }

    @Override
    public List<ShowGenre> readAll() {
        String sql = "SELECT * FROM [dbo].[ShowGenre]";

        List<ShowGenre> showGenres = jdbcClient.sql(sql)
            .query(ShowGenre.class)
            .list();
        
        return showGenres;
    }

    @Override
    public void update(UUID id, ShowGenre t) {
    }

    @Override
    public void delete(UUID... ids) {
    }
    
}
