package com.cineflex.API.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.API.model.Episode;

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
        String sql = "SELECT * FROM [dbo].[Episode] WHERE [Id] = ?";

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
        String sql = "SELECT * FROM [dbo].[Episode]";
        
        List<Episode> episodes = jdbcClient
            .sql(sql)
            .query(Episode.class)
            .list();

        return episodes;
    }

    @Override
    public void update(UUID id, Episode t) {
        String sql = "UPDATE [dbo].[Episode] SET [Title] = ?, [Number] = ?, [Description] = ?, [Url] = ?, [ReleaseDate] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [Duration] = ?, [OpeningStart] = ?, [OpeningEnd] = ?, [View] = ?, [Season] = ? WHERE [Id] = ?";

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
        String sql = "DELETE FROM [dbo].[Episode] WHERE [Id] IN (:ids)";

        int row = jdbcClient.sql(sql).param("ids", ids).update();

        if (row == 0) {
            throw new RuntimeException("Cannot add episode to database");
        }
    }

    public List<Episode> getBySeason(UUID... ids) {
        String sql = "SELECT * FROM [dbo].[Episode] WHERE [Season] IN :ids";
        
        List<Episode> episodes = jdbcClient
            .sql(sql)
            .param("ids", ids)
            .query(Episode.class)
            .list();

        return episodes;
    }

    public void deleteBySeason(UUID id) {
        String sql = "DELETE FROM [dbo].[Episode] WHERE [Season] = ?";

        int row = jdbcClient
            .sql(sql)
            .params(id)
            .update();
        
        if (row == 0) {
            throw new RuntimeException("Cannot delete episode(s)");
        }
    }
    
}
