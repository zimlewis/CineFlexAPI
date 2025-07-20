package com.cineflex.api.repository;

import java.util.ArrayList;
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

        jdbcClient.sql(sql)
            .params(
                t.getId(),
                t.getTitle(),
                t.getReleaseDate(),
                t.getCreatedTime(),
                t.getUpdatedTime(),
                t.getDescription(),
                t.getShow()
            ).update();    
    }

    @Override
    public Season read(UUID id) {
        String sql = "SELECT * FROM [dbo].[Season] WHERE [Id] = ? AND [IsDeleted] = 0";

        Season season = jdbcClient
            .sql(sql)
            .params(id)
            .query(Season.class)
            .optional()
            .orElse(null);
        
        return season;
    }

    @Override
    public List<Season> readAll(Integer page, Integer size) {
        String sql = "SELECT * FROM [dbo].[Season] WHERE [IsDeleted] = 0 ORDER BY [CreatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<Season> seasons = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(Season.class)
            .list();
        
        return seasons;
    }

    @Override
    public void update(UUID id, Season t) {
        String sql = "UPDATE [dbo].[Season] SET [Title] = ?, [ReleaseDate] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [Description] = ?, [Show] = ? WHERE [Id] = ? AND [IsDeleted] = 0";

        jdbcClient.sql(sql)
            .params(
                t.getTitle(),
                t.getReleaseDate(),
                t.getCreatedTime(),
                t.getUpdatedTime(),
                t.getDescription(),
                t.getShow(),
                id
            ).update();
    }

    @Override
    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "UPDATE [dbo].[Season] SET [IsDeleted] = 1 WHERE [Id] IN (" + placeholders + ")";

        jdbcClient.sql(sql).params(Arrays.asList(ids)).update();  
    }

    public List<Season> getByShow(UUID... ids) {
        return getByShow(0, 5, ids);
    }

    public List<Season> getByShow(Integer page, Integer size, UUID... ids) {
        if (ids.length == 0) return List.of(); // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "SELECT * FROM [dbo].[Season] WHERE [Show] IN (" + placeholders + ") AND [IsDeleted] = 0 ORDER BY [CreatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(ids));
        params.add(page * size);
        params.add(size);

        List<Season> seasons = jdbcClient
            .sql(sql)
            .params(params)
            .query(Season.class)
            .list();

        return seasons;
    }

    @Override
    public List<Season> readAll() {
        return readAll(0, 5);
    }

    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Season] WHERE [IsDeleted] = 0";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
    }

    public Integer getPageCountByShow(Integer size, UUID ...ids) {
        if (ids.length == 0) return -1; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));
        
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Season] WHERE [Show] IN (" + placeholders + ") AND [IsDeleted] = 0";

        List<Object> params = new ArrayList<>();

        params.add(size);
        params.addAll(params);

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(params)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;       
    }

}
