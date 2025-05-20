package com.cineflex.API.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.API.model.Genre;

@Repository
public class GenreRepository implements RepositoryInterface<Genre>{
    private final JdbcClient jdbcClient;

    public GenreRepository(
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public void create(Genre t) {
        String sql = "INSERT INTO [dbo].[Genre]([Id], [Name]) VALUES (?, ?)";

        int row = jdbcClient.sql(sql).params(
            t.getId(),
            t.getName()
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot add genre to database");
        }
    }

    @Override
    public Genre read(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public List<Genre> readAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAll'");
    }

    @Override
    public void update(UUID id, Genre t) {
        String sql = "UPDATE [dbo].[Genre] SET [Name] = ? WHERE [Id] = ?";

        int row = jdbcClient.sql(sql).params(
            t.getName(),
            t.getId()
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot update genre to database");
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM [dbo].[Genre] WHERE [Id] = ?";

        int row = jdbcClient.sql(sql).params(
            id
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot delete genre from database");
        }
    }
    
}
