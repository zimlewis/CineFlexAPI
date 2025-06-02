package com.cineflex.api.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Season;

@Repository
public class SeasonRepository implements RepositoryInterface<Season> {
    private final JdbcClient jdbcClient;

    public SeasonRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(Season t) {
        String sql = "INSERT INTO [dbo].[Season] ([Id], [Title], [ReleaseDate], [CreatedTime], [UpdatedTime], [Description], [Show]) VALUES (?, ?, ?, ?, ?, ?, ?)";

        int row = jdbcClient.sql(sql)
            .params(
                t.getId(),
                t.getTitle(),
                t.getReleaseDate(),
                t.getCreatedTime(),
                t.getUpdatedTime(),
                t.getDescription(),
                t.getShow()
            ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot add season to database");
        }        
    }

    @Override
    public Season read(UUID id) {
        String sql = "SELECT * FROM [dbo].[Season] WHERE [Id] = ?";

        Season season = jdbcClient
            .sql(sql)
            .params(id)
            .query(Season.class)
            .optional()
            .orElse(null);
        
        return season;
    }

    @Override
    public List<Season> readAll() {
        String sql = "SELECT * FROM [dbo].[Season]";

        List<Season> seasons = jdbcClient
            .sql(sql)
            .query(Season.class)
            .list();
        
        return seasons;
    }

    @Override
    public void update(UUID id, Season t) {
        String sql = "UPDATE [dbo].[Season] SET [Title] = ?, [ReleaseDate] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [Description] = ?, [Show] = ? WHERE [Id] = ?";

        int row = jdbcClient.sql(sql)
            .params(
                t.getTitle(),
                t.getReleaseDate(),
                t.getCreatedTime(),
                t.getUpdatedTime(),
                t.getDescription(),
                t.getShow(),
                id
            ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot update season to database");
        }   
    }

    @Override
    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "DELETE FROM [dbo].[Season] WHERE [Id] IN (" + placeholders + ")";

        int row = jdbcClient.sql(sql).params(Arrays.asList(ids)).update();

        if (row == 0) {
            throw new RuntimeException("Cannot remove season to database");
        }   
    }

    public List<Season> getByShow(UUID... ids) {
        if (ids.length == 0) return List.of(); // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "SELECT * FROM [dbo].[Season] WHERE [Show] IN (" + placeholders + ")";

        List<Season> seasons = jdbcClient
            .sql(sql)
            .params(Arrays.asList(ids))
            .query(Season.class)
            .list();

        return seasons;
    }

}
