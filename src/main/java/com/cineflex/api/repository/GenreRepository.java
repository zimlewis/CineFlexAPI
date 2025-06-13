package com.cineflex.api.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Genre;

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
        String sql = "INSERT INTO [dbo].[Genre] ([Id], [Name]) VALUES (?, ?)";

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
        String sql = "SELECT * FROM [dbo].[Genre] WHERE [Id] = ?";

        Genre genre = jdbcClient
            .sql(sql)
            .params(id)
            .query(Genre.class)
            .optional()
            .orElse(null);

        return genre;
    }

    @Override
    public List<Genre> readAll() {
        String sql = "SELECT * FROM [dbo].[Genre]";

        List<Genre> genres = jdbcClient.sql(sql).query(Genre.class).list();

        return genres;
    }

    @Override
    public void update(UUID id, Genre t) {
        String sql = "UPDATE [dbo].[Genre] SET [Name] = ? WHERE [Id] = ?";

        int row = jdbcClient.sql(sql).params(
            t.getName(),
            id
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot update genre to database");
        }
    }

    @Override
    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "DELETE FROM [dbo].[Genre] WHERE [Id] IN (" + placeholders + ")";

        int row = jdbcClient.sql(sql).params(Arrays.asList(ids)).update();

        if (row == 0) {
            throw new RuntimeException("Cannot delete genre from database");
        }
    }

    public List<Genre> readMultipleGenres(UUID... ids) {
        if (ids.length == 0) return List.of(); // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));
        
        String sql = "SELECT * FROM [dbo].[Genre] WHERE [Id] IN (" + placeholders + ")";

        List<Genre> genres = jdbcClient.sql(sql).params(Arrays.asList(ids)).query(Genre.class).list();

        return genres;
    }

    public List<UUID> getNotInTable(UUID... ids) {
        if (ids.length == 0) return List.of(); // avoid syntax error


        // Build VALUES string: "(?), (?), ..." and parameter array
        String placeholders = Arrays.stream(ids)
            .map(_ -> "(?)")
            .collect(Collectors.joining(", "));

        String sql = "SELECT input_ids.id FROM (VALUES " + placeholders + ") AS input_ids(id) LEFT JOIN [dbo].[Genre] ON input_ids.id = [dbo].[Genre].[Id] WHERE [dbo].[Genre].[Id] IS NULL";

        List<UUID> missignId = jdbcClient.sql(sql).params(Arrays.asList(ids)).query(UUID.class).list();

        return missignId;
    }

    public List<UUID> getIdsByNames(String... names) {
        if (names.length == 0) return List.of();

        String placeholders = Arrays.stream(names)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));
        
        String sql = "SELECT [Id] FROM [dbo].[Genre] WHERE [Name] IN ("+ placeholders +")";

        List<UUID> ids = jdbcClient.sql(sql).params(Arrays.asList(names)).query(UUID.class).list();
        
        return ids;
    }

    
}
