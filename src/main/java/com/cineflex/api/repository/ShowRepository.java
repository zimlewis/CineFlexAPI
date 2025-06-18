package com.cineflex.api.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Genre;
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

    
    public List<Genre> getGenres(UUID id) {
        String sql = "SELECT g.* FROM [dbo].[Genre] AS g LEFT JOIN [dbo].[ShowGenre] AS sg ON g.[Id] = sg.[Genre] WHERE sg.[Show] = ?";

        List<Genre> genres = jdbcClient
            .sql(sql)
            .param(id)
            .query(Genre.class)
            .list();

        return genres;
    }

    public List<Genre> addGenres(UUID show, UUID... genres) {
        String placeholder = Arrays.stream(genres)
            .map(_ -> "(?, ?)")
            .collect(Collectors.joining(", "));

        if (placeholder.trim().isEmpty()) {
            throw new ResponseStatusException(
              HttpStatus.NOT_ACCEPTABLE,
              "You either added an already added genre to this show or an empty value"  
            );
        }

        String sql = "INSERT INTO [dbo].[ShowGenre] ([Show], [Genre]) VALUES " + placeholder;
        
        List<UUID> params = new ArrayList<>();

        for (UUID genre : genres) {
            params.add(show);
            params.add(genre);
        }

        int row = jdbcClient.sql(sql).params(params).update();

        if (row == 0) {
            throw new RuntimeException("Cannot add genres to show");
        }

        placeholder = Arrays.stream(genres)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));
        
        sql = "SELECT * FROM [dbo].[Genre] WHERE [Id] IN (" + placeholder + ")";

        List<Genre> addedToShowGenre = jdbcClient.sql(sql).params(Arrays.asList(genres)).query(Genre.class).list();

        return addedToShowGenre;

    }

    public List<Show> showByGenres(UUID... genres) {
        String placeHolder = Arrays.stream(genres)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));
        
        if (placeHolder.trim().isEmpty()) {
            return List.of();
        }

        String sql = "SELECT * FROM [dbo].[Show] WHERE [Id] IN (SELECT [Show] FROM [dbo].[ShowGenre] WHERE [Genre] IN ("+ placeHolder +") GROUP BY [Show] HAVING COUNT(DISTINCT [Genre]) = ?)";
    
        List<Show> shows = jdbcClient.sql(sql).params(Arrays.asList(genres)).param(genres.length).query(Show.class).list();

        return shows;
    }

}
