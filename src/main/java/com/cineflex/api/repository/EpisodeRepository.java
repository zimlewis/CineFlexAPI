package com.cineflex.api.repository;

import java.util.ArrayList;
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
        String sql = "INSERT INTO [dbo].[Episode] ([Id], [Title], [Number], [Description], [Url], [ReleaseDate], [CreatedTime], [UpdatedTime], [Duration], [OpeningStart], [OpeningEnd], [View], [Season], [CommentSection]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcClient.sql(sql).params(
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
            t.getSeason(),
            t.getCommentSection()
        ).update();

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
    public List<Episode> readAll(Integer page, Integer size) {
        String sql = "SELECT * FROM [dbo].[Episode] WHERE [IsDeleted] = 0 ORDER BY [CreatedTime] OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        
        List<Episode> episodes = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(Episode.class)
            .list();

        return episodes;
    }

    @Override
    public void update(UUID id, Episode t) {
        String sql = "UPDATE [dbo].[Episode] SET [Title] = ?, [Number] = ?, [Description] = ?, [Url] = ?, [ReleaseDate] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [Duration] = ?, [OpeningStart] = ?, [OpeningEnd] = ?, [View] = ?, [Season] = ? WHERE [Id] = ? AND [IsDeleted] = 0";

        jdbcClient.sql(sql).params(
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

    }

    @Override
    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "UPDATE [dbo].[Episode] SET [IsDeleted] = 1 WHERE [Id] IN (" + placeholders + ")";

        jdbcClient.sql(sql).params(Arrays.asList(ids)).update();


    }

    public List<Episode> getBySeason(UUID ...ids) {
        return getBySeason(0, 5, ids);
    }

    public List<Episode> getBySeason(Integer page, Integer size, UUID... ids) {
        if (ids.length == 0) return List.of(); // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "SELECT * FROM [dbo].[Episode] WHERE [Season] IN (" + placeholders + ") AND [IsDeleted] = 0 ORDER BY [ReleaseDate] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        
        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(ids));
        params.add(page * size);
        params.add(size);

        List<Episode> episodes = jdbcClient
            .sql(sql)
            .params(params)
            .query(Episode.class)
            .list();

        return episodes;
    }

    @Override
    public List<Episode> readAll() {
        return readAll(0, 5);
    }

    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Episode] WHERE [IsDeleted] = 0";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
    }

    public Integer getPageCountBySeason(Integer size, UUID ...ids) {
        if (ids.length == 0) return -1; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Episode] WHERE [Season] IN (" + placeholders + ") AND [IsDeleted] = 0"; 

        List<Object> params = new ArrayList<>();
        params.add(size);
        params.addAll(Arrays.asList(ids));
        

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(params)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
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

    public List<Episode> getLikedEpisodeDetails(UUID account) {
        String sql = """
        SELECT e.*
        FROM [dbo].[Episode] e
        INNER JOIN [dbo].[Like] l ON e.id = l.episode
        WHERE l.account = ?
    """;

        return jdbcClient.sql(sql)
                .param(account)
                .query(Episode.class)
                .list();
    }
    
}
