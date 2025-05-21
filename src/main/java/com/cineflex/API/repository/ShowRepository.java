package com.cineflex.API.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.API.model.Show;

@Repository
public class ShowRepository implements RepositoryInterface<Show> {
    private final JdbcClient jdbcClient;

    public ShowRepository(
            JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(Show t) {
        String sql = "INSERT INTO [dbo].[Show] ([Id], [Title], [Description], [ReleaseDate], [Thumbnail], [CreatedTime], [OnGoing], [IsSeries]) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int row = jdbcClient.sql(sql).params(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getReleaseDate(),
                t.getThumbnail(),
                t.getCreatedTime(),
                t.getOnGoing(),
                t.getIsSeries()).update();

        if (row == 0) {
            throw new RuntimeException("Cannot add show to database");
        }
    }

    @Override
    public Show read(UUID id) {
        String sql = "SELECT * FROM [dbo].[Show] WHERE [Id] = ?";

        Show show = jdbcClient
            .sql(sql)
            .params(id)
            .query(Show.class)
            .optional()
            .orElse(null);

        return show;
    }

    @Override
    public List<Show> readAll() {
        String sql = "SELECT * FROM [dbo].[Show]";

        List<Show> shows = jdbcClient
            .sql(sql)
            .query(Show.class)
            .list();

        return shows;
    }

    @Override
    public void update(UUID id, Show t) {
        String sql = "UPDATE [dbo].[Show] SET [Title] = ?, [Description] = ?, [ReleaseDate] = ?, [Thumbnail] = ?, [CreatedTime] = ?, [OnGoing] = ?, [IsSeries] = ? WHERE [Id] = ?";

        int row = jdbcClient.sql(sql).params(
                t.getTitle(),
                t.getDescription(),
                t.getReleaseDate(),
                t.getThumbnail(),
                t.getCreatedTime(),
                t.getOnGoing(),
                t.getIsSeries(),
                id).update();

        if (row == 0) {
            throw new RuntimeException("Cannot update show to database");
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM [dbo].[ShowRepository] WHERE [Id] = ?";

        int row = jdbcClient.sql(sql).params(
                id).update();

        if (row == 0) {
            throw new RuntimeException("Cannot delete show from database");
        }
    }

}
