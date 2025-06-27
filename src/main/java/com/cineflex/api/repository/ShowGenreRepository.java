package com.cineflex.api.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        jdbcClient
            .sql(sql)
            .params(t.getShow(), t.getGenre())
            .update();
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
    
    public void deleteByShow(UUID... shows) {
        

        String placeholders = Arrays.stream(shows)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "DELETE FROM [dbo].[ShowGenre] WHERE [Show] IN (" + placeholders + ")";
        
        jdbcClient.sql(sql).params(Arrays.asList(shows)).update();
    }

}
