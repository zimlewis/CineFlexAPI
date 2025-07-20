package com.cineflex.api.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Comment;

@Repository
public class CommentRepository implements RepositoryInterface<Comment> {
    private final JdbcClient jdbcClient;

    public CommentRepository(
            JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(Comment comment) {
        String sql = "INSERT INTO [dbo].[Comment] ([Id], [Content], [CreatedTime], [UpdatedTime], [Account], [Section]) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcClient.sql(sql).params(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedTime(),
                comment.getUpdatedTime(),
                comment.getAccount(),
                comment.getSection()).update();
    }

    @Override
    public Comment read(UUID id) {
        String sql = "SELECT * FROM [dbo].[Comment] WHERE [Id] = ? AND [IsDeleted] = 0";

        Comment comment = jdbcClient.sql(sql)
                .params(id)
                .query(Comment.class)
                .optional()
                .orElse(null);

        return comment;
    }

    @Override
    public List<Comment> readAll(Integer page, Integer size) {
        String sql = "SELECT * FROM [dbo].[Comment] WHERE [IsDeleted] = 0 ORDER BY [CreatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<Comment> comments = jdbcClient
                .sql(sql)
                .params(page * size, size)
                .query(Comment.class)
                .list();

        return comments;
    }

    @Override
    public void update(UUID id, Comment t) {
        String sql = "UPDATE [dbo].[Comment] SET [Content] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [Account] = ?, [Section] = ? WHERE [Id] = ? AND [IsDeleted] = 0";

        jdbcClient.sql(sql).params(
                t.getContent(),
                t.getCreatedTime(),
                t.getUpdatedTime(),
                t.getAccount(),
                t.getSection(),
                id).update();

    }

    @Override
    public void delete(UUID... ids) {
        if (ids.length == 0)
            return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
                .map(_ -> "?")
                .collect(Collectors.joining(", "));

        String sql = "UPDATE [dbo].[Comment] SET [IsDeleted] = 1 WHERE [Id] IN (" + placeholders + ")";

        jdbcClient.sql(sql).params(Arrays.asList(ids)).update();
    }

    public List<Comment> getCommentsByEpisode(UUID... ids) {
        return getCommentsByEpisode(0, 5, ids);
    }

    public List<Comment> getCommentsByShow(Integer page, Integer size, UUID... ids) {
        if (ids.length == 0) {
            return List.of();
        }

        String placeholders = Arrays.stream(ids)
                .map(_ -> "?")
                .collect(Collectors.joining(", "));

        String sql = """
                    SELECT * FROM [dbo].[Comment]
                    WHERE [Section] IN (
                        SELECT [CommentSection] FROM [dbo].[Show] WHERE [Id] IN (%s)
                    )
                    AND [IsDeleted] = 0
                    ORDER BY [CreatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                """.formatted(placeholders);

        // Build parameters: [ids..., offset, size]
        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(ids)); // assume all ids are UUID/String
        params.add(page * size); // OFFSET
        params.add(size); // FETCH NEXT

        List<Comment> comments = jdbcClient
                .sql(sql)
                .params(params)
                .query(Comment.class)
                .list();

        return comments;
    }

    public Integer getPageCountByShow(Integer size, UUID... ids) {
        String placeholders = Arrays.stream(ids)
                .map((_) -> "?")
                .collect(Collectors.joining(", "));

        if (placeholders.trim().isEmpty()) {
            return -1;
        }

        String sql = "SELECT count(*)/? FROM [dbo].[Comment] WHERE [Section] = (SELECT [CommentSection] FROM [dbo].[Show] WHERE [Id] IN "
                + placeholders + ") AND [IsDeleted] = 0) AND [IsDeleted] = 0";

        List<Object> params = new ArrayList<>();

        params.add(size);
        params.addAll(params);

        Integer pageCount = jdbcClient
                .sql(sql)
                .params(params)
                .query(Integer.class).optional().orElse(0);

        return pageCount;
    }

    public List<Comment> getCommentsByEpisode(Integer page, Integer size, UUID... ids) {
        String placeholders = Arrays.stream(ids)
                .map((_) -> "?")
                .collect(Collectors.joining(", "));

        if (placeholders.trim().isEmpty()) {
            return List.of();
        }

        String sql = "SELECT * FROM [dbo].[Comment] WHERE [Section] IN (SELECT [CommentSection] FROM [dbo].[Episode] WHERE [Id] IN (%s)) AND [IsDeleted] = 0 ORDER BY [CreatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY".formatted(placeholders);

        List<Object> params = new ArrayList<>();
        params.addAll(Arrays.asList(ids)); // assume all ids are UUID/String
        params.add(page * size); // OFFSET
        params.add(size);        // FETCH NEXT

        List<Comment> comments = jdbcClient
            .sql(sql)
            .params(params)
            .query(Comment.class)
            .list();

        return comments;
    }

    @Override
    public List<Comment> readAll() {
        return readAll(0, 5);
    }

    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Comment] AND [IsDeleted] = 0";

        Integer pageCount = jdbcClient
                .sql(sql)
                .params(size)
                .query(Integer.class).optional().orElse(0);

        return pageCount;
    }

    public Integer getPageCountByEpisode(Integer size, UUID... ids) {
        String placeholders = Arrays.stream(ids)
                .map((_) -> "?")
                .collect(Collectors.joining(", "));

        if (placeholders.trim().isEmpty()) {
            return -1;
        }

        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Comment] WHERE [Section] IN (SELECT [CommentSection] FROM [dbo].[Episode] WHERE [Id] IN (%s)) AND [IsDeleted] = 0".formatted(placeholders);
        List<Object> params = new ArrayList<>();

        params.add(size);
        params.addAll(Arrays.asList(ids));

        Integer pageCount = jdbcClient
                .sql(sql)
                .params(params)
                .query(Integer.class).optional().orElse(-1);

        return pageCount;

    }
}
