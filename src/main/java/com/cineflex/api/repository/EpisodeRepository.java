package com.cineflex.api.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Episode;

@Repository
public class EpisodeRepository implements RepositoryInterface<Episode>{
    private final JdbcClient jdbcClient;

    public EpisodeRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(Episode t) {
        String sql = "INSERT INTO [dbo].[Episode] ([Id], [Title], [Number], [Description], [Url], [ReleaseDate], [CreatedTime], [UpdatedTime], [Duration], [OpeningStart], [OpeningEnd], [View], [Season]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int row = jdbcClient.sql(sql).params(
            t.getId(),
            t.getTitle(),
            t.getNumber(),
            t.getDescription(),
            t.getUrl(),
            t.getReleaseDate(),
            t.getCreatedTime(),
            t.getUpdatedTime(),
            t.getDuration(),
            t.getOpeningStart(),
            t.getOpeningEnd(),
            t.getView(),
            t.getSeason()
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot add episode to database");
        }

    }

    @Override
    public Episode read(UUID id) {
        String sql = "SELECT * FROM [dbo].[Episode] WHERE [Id] = ? AND [IsDeleted] = 0";

        Episode episode = jdbcClient
            .sql(sql)
            .params(id)
            .query(
                Episode.class
            ).optional()
            .orElse(null);

        return episode;
    }

    @Override
    public List<Episode> readAll() {
        String sql = "SELECT * FROM [dbo].[Episode] WHERE [IsDeleted] = 0";
        
        List<Episode> episodes = jdbcClient
            .sql(sql)
            .query(Episode.class)
            .list();

        return episodes;
    }

    @Override
    public void update(UUID id, Episode t) {
        String sql = "UPDATE [dbo].[Episode] SET [Title] = ?, [Number] = ?, [Description] = ?, [Url] = ?, [ReleaseDate] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [Duration] = ?, [OpeningStart] = ?, [OpeningEnd] = ?, [View] = ?, [Season] = ? WHERE [Id] = ? AND [IsDeleted] = 0";

        int row = jdbcClient.sql(sql).params(
            t.getTitle(),
            t.getNumber(),
            t.getDescription(),
            t.getUrl(),
            t.getReleaseDate(),
            t.getCreatedTime(),
            t.getUpdatedTime(),
            t.getDuration(),
            t.getOpeningStart(),
            t.getOpeningEnd(),
            t.getView(),
            t.getSeason(),
            id
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot add episode to database");
        }

    }

    @Override
    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "UPDATE [dbo].[Episode] SET [IsDeleted] = 1 WHERE [Id] IN (" + placeholders + ")";

        int row = jdbcClient.sql(sql).params(Arrays.asList(ids)).update();

        if (row == 0) {
            throw new RuntimeException("Cannot add episode to database");
        }
    }

    public List<Episode> getBySeason(UUID... ids) {
        if (ids.length == 0) return List.of(); // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "SELECT * FROM [dbo].[Episode] WHERE [Season] IN (" + placeholders + ") AND [IsDeleted] = 0";
        
        List<Episode> episodes = jdbcClient
            .sql(sql)
            .params(Arrays.asList(ids))
            .query(Episode.class)
            .list();

        return episodes;
    }

    // public void deleteBySeason(UUID id) {
    //     String sql = "DELETE FROM [dbo].[Episode] WHERE [Season] = ?";

    //     int row = jdbcClient
    //         .sql(sql)
    //         .params(id)
    //         .update();
        
    //     if (row == 0) {
    //         throw new RuntimeException("Cannot delete episode(s)");
    //     }
    // }
    
}
