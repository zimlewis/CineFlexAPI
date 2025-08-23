package com.cineflex.api.repository;

import java.util.List;
import java.util.UUID;

import com.cineflex.api.model.Show;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Favorite;

@Repository
public class FavoriteRepository implements RepositoryInterface<Favorite>{

    private final JdbcClient jdbcClient;

    public FavoriteRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(Favorite t) {
        String sql = "INSERT INTO [dbo].[Favorite] ([Account], [Show], [CreatedTime]) VALUES (?, ?, ?)";

        jdbcClient.sql(sql).params(
            t.getAccount(),
            t.getShow(),
            t.getCreatedTime()
        ).update();
    }

    @Override
    public Favorite read(UUID id) {
        return null;
    }

    @Override
    public List<Favorite> readAll(Integer page, Integer size) {
        String sql = "SELECT * FROM [dbo].[Favorite] ORDER BY [CreatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Favorite> favorites = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(Favorite.class)
            .list();

        return favorites;
    }

    @Override
    public void update(UUID id, Favorite t) {
    }

    @Override
    public void delete(UUID... ids) {

    }

    @Override
    public List<Favorite> readAll() {
        return readAll(0, 5);
    }

    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Favorite]";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(-1);

        return pageCount;
    }

    public Integer getFavoriteCount(UUID show) {
        String sql = "SELECT COUNT(*) FROM [dbo].[Favorite] WHERE [Show] = ?";

        Integer count = jdbcClient
            .sql(sql)
            .params(show)
            .query(Integer.class).optional().orElse(0);

        return count;
    }

    public void removeFavorite(UUID account, UUID show) {
        String sql = "DELETE FROM [dbo].[Favorite] WHERE [show] = ? AND [Account] = ?";

        jdbcClient.sql(sql).params(
            show,
            account
        ).update();
    }

    public Favorite getFavorite(UUID account, UUID show) {
        String sql = "SELECT * FROM [dbo].[Favorite] WHERE [Show] = ? AND [Account] = ?";

        Favorite favorite = jdbcClient
            .sql(sql)
            .params(
                show,
                account
            ).query(Favorite.class)
            .optional()
            .orElse(null);

        return favorite;
    }

    public List<Show> getFavoriteShowsByAccount(UUID account, int page, int size) {
        String sql = """
            SELECT s.*
            FROM [dbo].[Favorite] f
            JOIN [dbo].[Show] s ON f.[Show] = s.[Id]
            WHERE f.[Account] = ?
            ORDER BY f.[CreatedTime] DESC
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
            """;

        return jdbcClient.sql(sql)
                .params(account, page * size, size)
                .query(Show.class)
                .list();
    }

    public Integer getFavoritesPageCount(UUID account, int size) {
        String sql = "SELECT CEILING(COUNT(*) * 1.0 / ?) FROM [dbo].[Favorite] WHERE [Account] = ?";
        return jdbcClient.sql(sql)
                .params(size, account)
                .query(Integer.class)
                .optional()
                .orElse(0);
    }
        public List<Show> getMostFavoritedShows(int page, int size) {
            String sql = """
                SELECT s.[Id], s.[Title], s.[Description], s.[ReleaseDate],
                    s.[Thumbnail], s.[CreatedTime], s.[UpdatedTime],
                    s.[OnGoing], s.[IsSeries], s.[AgeRating],
                    s.[IsDeleted], s.[CommentSection]
                FROM [dbo].[Favorite] f
                JOIN [dbo].[Show] s ON f.[Show] = s.[Id]
                WHERE s.[IsDeleted] = 0
                GROUP BY s.[Id], s.[Title], s.[Description], s.[ReleaseDate],
                        s.[Thumbnail], s.[CreatedTime], s.[UpdatedTime],
                        s.[OnGoing], s.[IsSeries], s.[AgeRating],
                        s.[IsDeleted], s.[CommentSection]
                ORDER BY COUNT(*) DESC
                OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                """;

            return jdbcClient.sql(sql)
                    .params(page * size, size)
                    .query(Show.class)
                    .list();
        }
        public Integer getMostFavoritedShowsPageCount(int size) {
            String sql = "SELECT CEILING(COUNT(DISTINCT [Show]) * 1.0 / ?) FROM [dbo].[Favorite]";
            return jdbcClient.sql(sql)
                    .params(size)
                    .query(Integer.class)
                    .optional()
                    .orElse(0);
        }
    }



