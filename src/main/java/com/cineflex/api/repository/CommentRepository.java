package com.cineflex.api.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Comment;

@Repository
public class CommentRepository implements RepositoryInterface<Comment>{
    private final JdbcClient jdbcClient;

    public CommentRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(Comment comment) {
        String sql = "INSERT INTO [dbo].[Comment] ([Id], [Content], [CreatedTime], [UpdatedTime], [Account], [Episode]) VALUES (?, ?, ?, ?, ?, ?)";

        int row = jdbcClient.sql(sql).params(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedTime(),
            comment.getUpdatedTime(),
            comment.getAccount(),
            comment.getEpisode()
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot added comment to the database");
        }
    }

    @Override
    public Comment read(UUID id) {
        String sql = "SELECT * FROM [dbo].[Comment] WHERE [Id] = ?";

        Comment comment = jdbcClient.sql(sql)
            .params(id)
            .query(Comment.class)
            .optional()
            .orElse(null);

        return comment;
    }

    @Override
    public List<Comment> readAll() {
        String sql = "SELECT * FROM [dbo].[Comment]";
        
        List<Comment> comments = jdbcClient
            .sql(sql)
            .query(Comment.class)
            .list(); 

        return comments;
    }

    @Override
    public void update(UUID id, Comment t) {
        String sql = "UPDATE [dbo].[Comment] SET [Content] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [Account] = ?, [Episode] = ? WHERE [Id] = ?";
        
        int row = jdbcClient.sql(sql).params(
            t.getContent(),
            t.getCreatedTime(),
            t.getUpdatedTime(),
            t.getAccount(),
            t.getEpisode(),
            id
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot update comment");
        }

    }

    @Override
    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "DELETE FROM [dbo].[Comment] WHERE [Id] IN (" + placeholders + ")";

        int row = jdbcClient.sql(sql).params(Arrays.asList(ids)).update();

        if (row == 0) {
            throw new RuntimeException("Cannot delete comment");
        }
    }

    public List<Comment> getCommentsByEpisode(UUID ...ids) {
        String placeholders = Arrays.stream(ids)
            .map((_) -> "?")
            .collect(Collectors.joining(", "));

        String sql = "SELECT * FROM [dbo].[Comment] WHERE [Episode] IN (" + placeholders + ")";

        List<Comment> comments = jdbcClient.sql(sql).params(Arrays.asList(ids)).query(Comment.class).list();

        return comments;
    }
}
