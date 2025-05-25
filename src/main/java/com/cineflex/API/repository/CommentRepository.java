package com.cineflex.API.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.API.model.Comment;

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
        String sql = "DELETE FROM [dbo].[Comment] WHERE [Id] IN (:ids)";

        int row = jdbcClient.sql(sql).param("ids", ids).update();

        if (row == 0) {
            throw new RuntimeException("Cannot delete comment");
        }
    }
}
