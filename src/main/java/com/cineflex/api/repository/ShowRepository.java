package com.cineflex.api.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Show;

@Repository
public class ShowRepository implements RepositoryInterface<Show> {
    private final JdbcClient jdbcClient;

    public ShowRepository(
            JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(Show t) {
        String sql = "INSERT INTO [dbo].[Show] ([Id], [Title], [Description], [ReleaseDate], [Thumbnail], [CreatedTime], [UpdatedTime], [OnGoing], [IsSeries], [AgeRating]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int row = jdbcClient.sql(sql).params(
            t.getId(),
            t.getTitle(),
            t.getDescription(),
            t.getReleaseDate(),
            t.getThumbnail(),
            t.getCreatedTime(),
            t.getUpdatedTime(),
            t.getOnGoing(),
            t.getIsSeries(),
            t.getAgeRating()
        ).update();

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
        String sql = "UPDATE [dbo].[Show] SET [Title] = ?, [Description] = ?, [ReleaseDate] = ?, [Thumbnail] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [OnGoing] = ?, [IsSeries] = ?, [AgeRating] = ? WHERE [Id] = ?";

        int row = jdbcClient.sql(sql).params(
            t.getTitle(),
            t.getDescription(),
            t.getReleaseDate(),
            t.getThumbnail(),
            t.getCreatedTime(),
            t.getUpdatedTime(),
            t.getOnGoing(),
            t.getIsSeries(),
            t.getAgeRating(),
            id
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot update show to database");
        }
    }

    @Override
    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "DELETE FROM [dbo].[Show] WHERE [Id] IN (" + placeholders + ")";

        int row = jdbcClient.sql(sql).params(Arrays.asList(ids)).update();

        if (row == 0) {
            throw new RuntimeException("Cannot delete show from database");
        }
    }

}
